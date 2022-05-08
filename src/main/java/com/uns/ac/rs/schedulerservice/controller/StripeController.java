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
import com.uns.ac.rs.schedulerservice.dto.stripe.CreatePayment;
import com.uns.ac.rs.schedulerservice.dto.stripe.CreatePaymentResponse;
import com.uns.ac.rs.schedulerservice.model.Payment;
import com.uns.ac.rs.schedulerservice.repository.PaymentRepository;
import com.uns.ac.rs.schedulerservice.repository.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@CrossOrigin
@RestController
public class StripeController {

    @Value("${stripe.webhook.secret}")
    private String endpointSecret;

    @Value("${stripe.apiKey}")
    private String apiKey;

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private ReservationRepository reservationRepository;

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

    @PostMapping("/stripe/events")
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
        // Handle the event
        switch (event.getType()) {
            case "payment_intent.succeeded":
                System.out.println("Payment succeeded and confirmed by Stripe!!!");
                payment = paymentRepository.findFirst1ByPaymentIntentAndStripConfirmed(intent,false);

                if (payment != null){
                    payment.setPaid(true); payment.setStripConfirmed(true); payment.setConfirmation(String.valueOf(System.currentTimeMillis()));
                    paymentRepository.save(payment);
                    //TODO - websocket to inform about recently made reservations
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
