package si.nimbostratuz.bikeshare.models.common;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
@Data
public class Location {

    @Column(name = "location_lat")
    private Double latitude;

    @Column(name = "location_lng")
    private Double longitude;
}
