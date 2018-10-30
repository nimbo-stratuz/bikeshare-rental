package si.nimbostratuz.bikeshare.models.common;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
@Data
public class Owner {

    @Column(name = "owner_id", nullable = false)
    private Integer id;
}
