package si.nimbostratuz.bikeshare.services.configuration;

import com.kumuluz.ee.configuration.cdi.ConfigBundle;
import com.kumuluz.ee.configuration.cdi.ConfigValue;

import javax.enterprise.context.ApplicationScoped;
import java.math.BigDecimal;

@ApplicationScoped
@ConfigBundle("app-properties")
public class Pricing {
    @ConfigValue(value = "price-per-minute", watch = true)
    private String pricePerMinute;
    @ConfigValue(value = "start-minutes", watch = true)
    private Integer startMinutes;

    public Integer getStartMinutes() {return startMinutes;}
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

    public void setStartMinutes(Integer startMinutes) { this.startMinutes = startMinutes; }
    public void setPricePerMinute (String pricePerMinute) {
        this.pricePerMinute = pricePerMinute;
    }
}
