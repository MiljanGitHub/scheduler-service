package com.uns.ac.rs.schedulerservice.model;

import lombok.Data;
import javax.persistence.*;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.hibernate.annotations.Type;
import lombok.experimental.SuperBuilder;

@Data
@Entity
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "reservations")
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
    @JoinColumn(name="court_id", nullable = false)
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
