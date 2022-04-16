package com.uns.ac.rs.schedulerservice.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Type;

import javax.persistence.*;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@Entity
@Table(name = "reservation")
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reservation_id", unique = true, nullable = false)
    private int id;

    @Column(name = "start", unique = false, nullable = true)
    private String start;

    @Column(name = "end", unique = false, nullable = true)
    private String end;

    @ManyToOne
    @JoinColumn(name = "court_id", referencedColumnName = "court_id", nullable = true)
    private Court court;

    @Column(name = "user_id", unique = false, nullable = true)
    private int userId;

    @Column(name = "paid", nullable = false)
    @Type(type = "org.hibernate.type.NumericBooleanType")
    private Boolean paid;

    @Column(name = "payment", unique = false, nullable = false)
    private double payment;

    @Column(name = "paid_at", unique = false, nullable = true)
    private String paidAt;

    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;
}
