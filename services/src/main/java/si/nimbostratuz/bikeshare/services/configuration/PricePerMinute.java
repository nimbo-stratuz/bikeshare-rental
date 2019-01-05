package si.nimbostratuz.bikeshare.services.configuration;

import com.kumuluz.ee.configuration.cdi.ConfigBundle;
import com.kumuluz.ee.configuration.cdi.ConfigValue;

import javax.enterprise.context.ApplicationScoped;
import java.math.BigDecimal;

@ApplicationScoped
@ConfigBundle("app-properties")
public class PricePerMinute {
    @ConfigValue(value = "price-per-minute", watch = true)
    private String pricePerMinute;


    public String getPricePerMinute() {
        return pricePerMinute;
    }

    /**
     *
     * @return pricePerMinute in BigDecimal type
     */
    public BigDecimal getBigDecimalPricePerMinute() {
        BigDecimal price =new BigDecimal(this.pricePerMinute);
        return price;
    }

    public void setPricePerMinute (String pricePerMinute) {
        this.pricePerMinute = pricePerMinute;
    }
}
