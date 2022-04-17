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
                        })} )
})
@NamedNativeQueries(value = {

        @NamedNativeQuery(name = "findAllCourtsInfo",
                          query = "SELECT c.court_id AS id, c.name AS name FROM Courts c",
                          resultSetMapping = "findCourtInfoMapping")

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
