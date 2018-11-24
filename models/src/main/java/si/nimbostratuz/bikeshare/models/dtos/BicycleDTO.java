package si.nimbostratuz.bikeshare.models.dtos;

import lombok.Data;

import java.time.Instant;

@Data
public class BicycleDTO {

    private Integer id;

    private String smartLockUUID;

    private LocationDTO location;

    private Boolean available;

    private Instant dateAdded;

    private Integer ownerId;
}
