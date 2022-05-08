package com.uns.ac.rs.schedulerservice.service.impl.validators;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import com.uns.ac.rs.schedulerservice.dto.request.ReservationRequest;
import com.uns.ac.rs.schedulerservice.dto.response.BookingDto;
import com.uns.ac.rs.schedulerservice.model.Court;
import com.uns.ac.rs.schedulerservice.model.Payment;
import com.uns.ac.rs.schedulerservice.model.PaymentMethod;
import com.uns.ac.rs.schedulerservice.model.Reservation;
import com.uns.ac.rs.schedulerservice.repository.CourtRepository;
import com.uns.ac.rs.schedulerservice.repository.PaymentRepository;
import com.uns.ac.rs.schedulerservice.repository.ReservationRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Order(Ordered.LOWEST_PRECEDENCE)
@Component
@RequiredArgsConstructor
public class ValidatorD extends Validator{

    private final PaymentRepository paymentRepository;
    private final CourtRepository courtRepository;
    private final ReservationRepository reservationRepository;

    @Value("${stripe.apiKey}")
    private String apiKey;

    @Override
    public BookingDto handle(ReservationRequest reservationRequest) {
        //ValidatorB -> Payment Service (if user has card data and enough founds) PaymentGateway

        String paymentMethod = reservationRequest.getPaymentMethod().toString();

        long total = calculatePayment(reservationRequest);

        if (paymentMethod.equals("CARD")){

            Payment newPayment = paymentRepository.save(Payment.builder()
                    .userId(reservationRequest.getUser())
                    .paid(false)
                    .payment(total)
                    .paymentMethod(PaymentMethod.CARD)
                    .paymentIntent(reservationRequest.getPaymentIntent())
                    .stripConfirmed(false)
                    .build());

            Court court = courtRepository.getById(reservationRequest.getCourtId());

            List<Reservation> reservations = reservationRepository.saveAll(reservationRequest.getReservationDtos().stream().map(reservationDto ->
                    Reservation.builder().payment(newPayment)
                            .court(court)
                            .start(reservationDto.getStart().toString())
                            .end(reservationDto.getEnd().toString()).build()).collect(Collectors.toList()));



            return BookingDto.builder().hasErrorMessage(false).successMessage("You successfully placed your reservations.").build();

        } else if (paymentMethod.equals("CASH")){

            Payment newPayment = paymentRepository.save(Payment.builder()
                            .userId(reservationRequest.getUser())
                            .paid(false)
                            .payment(total)
                            .paymentIntent(null)
                            .stripConfirmed(false)
                            .paymentMethod(PaymentMethod.CASH).build());

            Court court = courtRepository.getById(reservationRequest.getCourtId());

            List<Reservation> reservations = reservationRepository.saveAll(reservationRequest.getReservationDtos().stream().map(reservationDto ->
                    Reservation.builder().payment(newPayment)
                            .court(court)
                            .start(reservationDto.getStart().toString())
                            .end(reservationDto.getEnd().toString()).build()).collect(Collectors.toList()));

            return BookingDto.builder().hasErrorMessage(false).successMessage("You successfully placed your reservations.").build();
        }


        throw new RuntimeException("Error!");
    }

    private long calculatePayment(ReservationRequest reservationRequest){
        return reservationRequest.getReservationDtos().size() == 1 ? 5 : (reservationRequest.getReservationDtos().size() - 1) * 10L + 5;
    }

    private String initIntent(Long amount){

        Stripe.apiKey = apiKey;

        //CreatePayment postBody = gson.fromJson(request.body(), CreatePayment.class);
        PaymentIntentCreateParams params =
                PaymentIntentCreateParams.builder()
                        .setAmount(amount)
                        .setCurrency("eur")
                        .setAutomaticPaymentMethods(
                                PaymentIntentCreateParams.AutomaticPaymentMethods
                                        .builder()
                                        .setEnabled(true)
                                        .build()
                        )
                        .build();

        // Create a PaymentIntent with the order amount and currency
        try {
            PaymentIntent paymentIntent = PaymentIntent.create(params);
            return paymentIntent.getClientSecret();

        } catch (StripeException stripeException){

        }

        throw new RuntimeException("Error creating intent");
    }
}
