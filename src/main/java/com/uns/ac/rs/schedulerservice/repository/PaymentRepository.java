package com.uns.ac.rs.schedulerservice.repository;

import com.uns.ac.rs.schedulerservice.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Integer> {

    public Payment findFirst1ByPaymentIntentAndStripConfirmed(String paymentIntent, Boolean stripConfirmed);
}
