package si.nimbostratuz.bikeshare.models.dtos;

import lombok.Data;
import si.nimbostratuz.bikeshare.models.common.Location;

import java.time.Instant;

@Data
public class BicycleDTO {

    private Integer id;

    private String smartLockUUID;

    private Location location;

    private Boolean available;

    private Instant dateAdded;

    private Integer ownerId;
}
