package com.uns.ac.rs.schedulerservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "courts")

@SqlResultSetMappings({


        @SqlResultSetMapping(name = "findCourtInfoMapping",
                classes = {@ConstructorResult(targetClass=com.uns.ac.rs.schedulerservice.dto.response.CourtInfo.class,
                columns = {@ColumnResult(name="id", type=Integer.class),
                           @ColumnResult(name="name", type=String.class)
                        })} ),
        @SqlResultSetMapping(name = "findCourtReservationInfoMapping",
                classes = {@ConstructorResult(targetClass=com.uns.ac.rs.schedulerservice.dto.response.CourtData.class,
                columns = {@ColumnResult(name="courtId", type=Integer.class),
                           @ColumnResult(name="courtName", type=String.class),
                           @ColumnResult(name="reservationId", type=Integer.class),
                           @ColumnResult(name="start", type=String.class),
                           @ColumnResult(name="end", type=String.class)
                          })} )
})
@NamedNativeQueries(value = {

        @NamedNativeQuery(name = "findAllCourtsInfo",
                query = "SELECT c.court_id AS id, c.name AS name FROM Courts c",
                resultSetMapping = "findCourtInfoMapping"),

        @NamedNativeQuery(name = "findCourtReservationInfo",
                query = "SELECT c.court_id AS courtId, c.name AS courtName, reservations.reservation_id as reservationId, reservations.start AS start, reservations.end AS end " +
                        "FROM courts c " +
                        "LEFT JOIN reservations ON reservations.court_id " +
                        "WHERE c.court_id = reservations.court_id AND c.court_id = :courtId AND reservations.start + 0 >= :now " +
                        "ORDER BY reservations.start + 0 ASC;",
                resultSetMapping = "findCourtReservationInfoMapping")
})
//@NamedQuery(name = "Courts.findAll", query = "SELECT * FROM Courts c")
public class Court {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "court_id", unique = true, nullable = false)
    private int id;

    @Column(name = "name", unique = true, nullable = false)
    private String name;

    @Column(name = "created")
    private String created;

    @Column(name = "_active")
    private boolean active;

    //@Column(name = "_type")
    //private String type;

    //private byte[] image;

    @OneToMany(mappedBy = "court") //, cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true
    private List<Reservation> reservations = new ArrayList<>();
}
