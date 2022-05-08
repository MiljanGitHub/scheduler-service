package com.uns.ac.rs.schedulerservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "payment")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_id", unique = true, nullable = false)
    private int id;

    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;

    @Column(name = "user_id", unique = false, nullable = true)
    private int userId;

    @Column(name = "payment_intent", unique = true, nullable = true)
    private String paymentIntent;

    @Column(name = "strip_confirmed", nullable = false)
    @Type(type = "org.hibernate.type.NumericBooleanType")
    private Boolean stripConfirmed;

    @Column(name = "paid", nullable = false)
    @Type(type = "org.hibernate.type.NumericBooleanType")
    private Boolean paid;

    @Column(name = "payment", unique = false, nullable = false)
    private Long payment;

    @Column(name = "confirmation", unique = false, nullable = true)
    private String confirmation;

    @OneToMany(mappedBy = "payment") //, cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true
    private List<Reservation> reservations;


}
