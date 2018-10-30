package si.nimbostratuz.bikeshare.models.dtos;

import lombok.Data;
import si.nimbostratuz.bikeshare.models.common.Location;

import java.util.Date;

@Data
public class RentalDTO {

    private Integer id;

    private Date rentStart;

    private Date rentEnd;

    private Location startLocation;

    private Location endLocation;

    private Integer userId;

    private Integer bicycleId;
}
