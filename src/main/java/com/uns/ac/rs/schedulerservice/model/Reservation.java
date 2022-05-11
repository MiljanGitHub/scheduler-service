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
@SqlResultSetMappings({


        @SqlResultSetMapping(name = "findByCourtMapping",
                classes = {@ConstructorResult(targetClass=com.uns.ac.rs.schedulerservice.dto.response.ReservationDto.class,
                columns = {@ColumnResult(name="reservationId", type=Integer.class),
                           @ColumnResult(name="start", type=String.class),
                           @ColumnResult(name="end", type=String.class)
                        })} ),

        @SqlResultSetMapping(name = "findReservationsByCourtAndUserMapping",
                classes = {@ConstructorResult(targetClass=com.uns.ac.rs.schedulerservice.dto.response.ReservationByCourtAndUser.class,
                        columns = {@ColumnResult(name="reservationId", type=Integer.class),
                                   @ColumnResult(name="start", type=String.class),
                                   @ColumnResult(name="end", type=String.class),
                                   @ColumnResult(name="paymentType", type=String.class),
                                   @ColumnResult(name="paid", type=Boolean.class)
                        })} )

})
@NamedNativeQueries(value = {

        @NamedNativeQuery(name = "findByCourt",
                query = "select r.reservation_id as reservationId, r.start as start, r.end as end from reservations r where r.court_id = :courtId AND (r.start + 0) >= :now ORDER BY (r.start + 0) asc",
                resultSetMapping = "findByCourtMapping"),

        @NamedNativeQuery(name = "findReservationsByCourtAndUser",
                query = "SELECT r.reservation_id as reservationId, r.start as start, r.end as end, payment.payment_method as paymentType, payment.paid as paid " +
                        "FROM reservations r " +
                        "LEFT JOIN payment ON payment.payment_id " +
                        "WHERE payment.user_id = :userId AND r.court_id = :courtId " +
                        "ORDER BY r.start + 0 ASC;",
                resultSetMapping = "findReservationsByCourtAndUserMapping")
})
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
    @JoinColumn(name="court_id", nullable = true)
    private Court court;

    @ManyToOne
    @JoinColumn(name="payment_id", nullable = true)
    private Payment payment;

   /* @Column(name = "user_id", unique = false, nullable = true)
    private int userId;

    @Column(name = "paid", nullable = false)
    @Type(type = "org.hibernate.type.NumericBooleanType")
    private Boolean paid;

    @Column(name = "payment", unique = false, nullable = false)
    private double payment;

    @Column(name = "paid_at", unique = false, nullable = true)
    private String paidAt;

    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;*/
}
