package com.uns.ac.rs.schedulerservice.controller;

import com.stripe.Stripe;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.exception.StripeException;
import com.stripe.model.Event;
import com.stripe.model.EventDataObjectDeserializer;
import com.stripe.model.PaymentIntent;
import com.stripe.model.StripeObject;
import com.stripe.net.Webhook;
import com.stripe.param.PaymentIntentCreateParams;
import com.uns.ac.rs.schedulerservice.configuration.JmsConfig;
import com.uns.ac.rs.schedulerservice.dto.stripe.CreatePayment;
import com.uns.ac.rs.schedulerservice.dto.stripe.CreatePaymentResponse;
import com.uns.ac.rs.schedulerservice.model.Payment;
import com.uns.ac.rs.schedulerservice.model.Reservation;
import com.uns.ac.rs.schedulerservice.repository.PaymentRepository;
import com.uns.ac.rs.schedulerservice.repository.ReservationRepository;
import common.events.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@CrossOrigin
@RestController
@RequestMapping("/stripe")
public class StripeController {

    @Value("${stripe.webhook.secret}")
    private String endpointSecret;

    @Value("${stripe.apiKey}")
    private String apiKey;

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private JmsTemplate jmsTemplate;

    @PostMapping("/create-payment-intent")
    public CreatePaymentResponse createStripIntent(@RequestBody CreatePayment createPayment) {

        // This is a public sample test API key.
        Stripe.apiKey = apiKey;

            PaymentIntentCreateParams params =
                    PaymentIntentCreateParams.builder()
                            .setAmount(createPayment.getAmount()*100)
                            .setCurrency("eur")
                            .setPaymentMethod("pm_card_visa")
                            .build();
        // Create a PaymentIntent with the order amount and currency
        try {
            PaymentIntent  paymentIntent = PaymentIntent.create(params);
            return new CreatePaymentResponse(paymentIntent.getClientSecret());

        } catch (StripeException stripeException){
            System.out.println(stripeException.getStripeError());
        }
        throw new RuntimeException("Payment Intent creation occurred");
    }

    @PostMapping("/events")
    public ResponseEntity<?> test(@RequestBody String payload,@RequestHeader("Stripe-Signature") String sigHeader ){
        Event event = null;

        if(endpointSecret != null && sigHeader != null) {
            // Only verify the event if you have an endpoint secret defined.
            // Otherwise use the basic event deserialized with GSON.
            try {
                event = Webhook.constructEvent(payload, sigHeader, endpointSecret);
            } catch (SignatureVerificationException e) {
                System.out.println("Webhook error while validating signature.");
                System.out.println(e.getStripeError());
                return ResponseEntity.internalServerError().body(Optional.empty());
            }
        }
        // Deserialize the nested object inside the event
        assert event != null;
        EventDataObjectDeserializer dataObjectDeserializer = event.getDataObjectDeserializer();
        StripeObject stripeObject = null;
        if (dataObjectDeserializer.getObject().isPresent()) {
            stripeObject = dataObjectDeserializer.getObject().get();
        }

        if (!(stripeObject instanceof PaymentIntent)) return ResponseEntity.ok(Optional.empty());

        PaymentIntent paymentIntent = (PaymentIntent) stripeObject;
        String intent = paymentIntent.getClientSecret();
        Payment payment = null;
        // Handle the Strip event ("webhooks")
        switch (event.getType()) {
            case "payment_intent.succeeded":
                System.out.println("Payment succeeded and confirmed by Stripe!!!");
                /*
                    Step 1: Find first Payment with PaymentIntent ('intent') given from Stripe and which are not yet confirmed (it means we still don't have information about whether
                            that payment is accepted or rejected). But since we are in this 'switch-case' it means it is accepted by Stripe.
                 */
                payment = paymentRepository.findFirst1ByPaymentIntentAndStripConfirmed(intent,false);

                if (payment != null){

                    /*
                      Step 2: Edit appropriate Payment's entity fields, so we know that 'payment' is accepted by Stripe
                     */
                    payment.setPaid(true); payment.setStripConfirmed(true); payment.setConfirmation(String.valueOf(System.currentTimeMillis()));
                    paymentRepository.save(payment);

                    /*
                      Step 3: Inform notification-service (which is our web-socket server), via ActiveMQ Artemis,
                              to query necessary services a make appropriate notifications on front-end, based on payment id.
                     */


                    System.out.println("treba da posalje: " + payment.getId());
                    jmsTemplate.convertAndSend(JmsConfig.PAYMENT_QUEUE, payment.getId());
                }
                break;
            case "payment_intent.payment_failed":
                System.out.println("Payment failed and declined by Stripe!!!");
                payment = paymentRepository.findFirst1ByPaymentIntentAndStripConfirmed(intent,false);

                if (payment != null){
                    payment.setPaid(false); payment.setStripConfirmed(true); payment.setConfirmation(String.valueOf(System.currentTimeMillis()));
                    paymentRepository.save(payment);
                }

                //delete reservations:
                assert payment != null;
                reservationRepository.deleteAllInBatch(payment.getReservations());

                break;
            default:
                System.out.println("Unhandled event type: " + event.getType());
                break;
        }
        return ResponseEntity.ok(Optional.empty());
    }


}
