package si.nimbostratuz.bikeshare.models.dtos;

import lombok.Data;

import java.util.Date;

@Data
public class BicycleDTO {

    private Integer id;

    private String smartLockUUID;

    private LocationDTO location;

    private Boolean available;

    private Date dateAdded;

    private Integer ownerId;
}
