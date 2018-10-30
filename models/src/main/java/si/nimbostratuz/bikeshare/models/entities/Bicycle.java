package si.nimbostratuz.bikeshare.models.entities;

import lombok.Data;
import si.nimbostratuz.bikeshare.models.common.Location;

import javax.persistence.*;
import java.util.Date;

@Entity(name = "bicycle")
@Data
@NamedQueries(value = {
        @NamedQuery(
                name = "Bicycle.getAll",
                query = "SELECT o FROM bicycle o"
        ),
        @NamedQuery(
                name = "Bicycle.findBySmartLockUUID",
                query = "SELECT o FROM bicycle o WHERE o.smartLockUUID = :smartLockUUID"
        )
})
public class Bicycle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "smart_lock_uuid", nullable = false, unique = true)
    private String smartLockUUID;

    @Embedded
    private Location location;

    @Column(name = "available", nullable = false)
    private Boolean available;

    @Column(name = "date_added", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateAdded;

    @Column(name = "owner_id", nullable = false)
    private Integer ownerId;
}
