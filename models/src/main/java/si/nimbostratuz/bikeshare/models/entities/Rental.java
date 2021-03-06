package si.nimbostratuz.bikeshare.models.entities;

import lombok.Data;
import si.nimbostratuz.bikeshare.models.common.Location;

import javax.persistence.*;
import java.util.Date;

@Entity(name = "rental")
@Data
@NamedQueries({
        @NamedQuery(name = "Rental.getAll", query = "SELECT r from rental r")
})
public class Rental {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "rent_start", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date rentStart;

    @Column(name = "rent_end")
    @Temporal(TemporalType.TIMESTAMP)
    private Date rentEnd;


    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "latitude", column = @Column(name = "start_location_lat")),
            @AttributeOverride(name = "longitude", column = @Column(name = "start_location_lng"))
    })
    private Location startLocation;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "latitude", column = @Column(name = "end_location_lat")),
            @AttributeOverride(name = "longitude", column = @Column(name = "end_location_lng"))
    })
    private Location endLocation;

    @Column(name = "user_id", nullable = false)
    private Integer userId;

    @Column(name = "bicycle_id", nullable = false)
    private Integer bicycleId;
}
