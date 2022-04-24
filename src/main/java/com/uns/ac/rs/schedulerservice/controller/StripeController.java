package com.uns.ac.rs.schedulerservice.controller;

import com.stripe.Stripe;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.exception.StripeException;
import com.stripe.model.*;
import com.stripe.net.Webhook;
import com.stripe.param.PaymentIntentCreateParams;
import com.uns.ac.rs.schedulerservice.dto.stripe.CreatePayment;
import com.uns.ac.rs.schedulerservice.dto.stripe.CreatePaymentResponse;
import com.uns.ac.rs.schedulerservice.model.Payment;
import com.uns.ac.rs.schedulerservice.model.Reservation;
import com.uns.ac.rs.schedulerservice.repository.PaymentRepository;
import com.uns.ac.rs.schedulerservice.repository.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@CrossOrigin
@RestController
public class StripeController {

    @Value("${stripe.webhook.secret}")
    private String endpointSecret;

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    @PostMapping("/create-payment-intent")
    public CreatePaymentResponse createStripIntent(@RequestBody CreatePayment createPayment) {

        // This is a public sample test API key.
        // Don’t submit any personally identifiable information in requests made with this key.
        // Sign in to see your own test API key embedded in code samples.
        Stripe.apiKey = "sk_test_51KrplgL8U3BNM2iQy3cCNphQG7vMdewdLMd71f6AljjRRdu7GGJLI8pabmSrBIObbcLMSLIdBqgpP8JFdLr4fmim00qF1P1QZ2";



            //CreatePayment postBody = gson.fromJson(request.body(), CreatePayment.class);
            PaymentIntentCreateParams params =
                    PaymentIntentCreateParams.builder()
                            .setAmount(createPayment.getAmount())
                            .setCurrency("eur")
                            .setAutomaticPaymentMethods(
                                    PaymentIntentCreateParams.AutomaticPaymentMethods
                                            .builder()
                                            .setEnabled(true)
                                            .build())
                            .build();

            // Create a PaymentIntent with the order amount and currency
        try {
            PaymentIntent  paymentIntent = PaymentIntent.create(params);
            return new CreatePaymentResponse(paymentIntent.getClientSecret());

        } catch (StripeException stripeException){

        }



        //    return gson.toJson(paymentResponse);

        return null;
    }

    @PostMapping("/stripe/events")
    public ResponseEntity<?> test(@RequestBody String payload,@RequestHeader("Stripe-Signature") String sigHeader ){


        Event event = null;


        if(endpointSecret != null && sigHeader != null) {
            // Only verify the event if you have an endpoint secret defined.
            // Otherwise use the basic event deserialized with GSON.
            try {
                event = Webhook.constructEvent(
                        payload, sigHeader, endpointSecret
                );
            } catch (SignatureVerificationException e) {
                // Invalid signature
                System.out.println("Webhook error while validating signature.");
                //response.status(400);
                return null;
            }
        }
        // Deserialize the nested object inside the event
        assert event != null;
        EventDataObjectDeserializer dataObjectDeserializer = event.getDataObjectDeserializer();
        StripeObject stripeObject = null;
        if (dataObjectDeserializer.getObject().isPresent()) {
            stripeObject = dataObjectDeserializer.getObject().get();
        }

        PaymentIntent paymentIntent = (PaymentIntent) stripeObject;
        String intent = paymentIntent.getClientSecret();
        Payment payment = null;
        // Handle the event
        switch (event.getType()) {
            case "payment_intent.succeeded":
                //nadji Payment gde je isti secret i gde nije confirmedByStripe
                payment = paymentRepository.findFirst1ByPaymentIntentAndStripConfirmed(intent,false);

                if (payment != null){
                    payment.setPaid(true); payment.setStripConfirmed(true);
                    paymentRepository.save(payment);
                }

                //Use socket service to let know FE that payment is okay

                break;
            case "charge.failed":

                //nadji Payment gde je isti secret i gde nije confirmedByStripe
                payment = paymentRepository.findFirst1ByPaymentIntentAndStripConfirmed(intent,false);

                if (payment != null){
                    payment.setPaid(false); payment.setStripConfirmed(true);
                    paymentRepository.save(payment);
                }

                //delete reservations:
                assert payment != null;
                reservationRepository.deleteAllInBatch(payment.getReservations());

                //Use socket service to let know FE that payment is not succeeded

                break;
            default:
                System.out.println("Unhandled event type: " + event.getType());
                break;
        }
        return ResponseEntity.ok(Optional.empty());
    }


}
