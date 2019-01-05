package si.nimbostratuz.bikeshare.models.dtos;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;


@Data
public class PaymentDTO {

    private Integer id;

    private Integer fromUserId;

    private Integer toUserId;

    private Date date;

    private Integer rideId;

    private BigDecimal amount;
}
