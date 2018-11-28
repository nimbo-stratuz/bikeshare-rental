package si.nimbostratuz.bikeshare.api.v1.health;

import com.kumuluz.ee.discovery.annotations.DiscoverService;
import org.eclipse.microprofile.health.Health;
import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;
import org.eclipse.microprofile.health.HealthCheckResponseBuilder;
import org.glassfish.jersey.client.ClientProperties;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.ProcessingException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import java.util.logging.Logger;

@Health
@ApplicationScoped
public class BikeshareCatalogueHealthCheck implements HealthCheck {

    private Logger log = Logger.getLogger(BikeshareCatalogueHealthCheck.class.getName());

    @Inject
    @DiscoverService("bikeshare-catalogue")
    private WebTarget catalogueWebTarget;

    @Override
    public HealthCheckResponse call() {

        HealthCheckResponseBuilder builder = HealthCheckResponse.named(BikeshareCatalogueHealthCheck.class.getSimpleName());

        log.info("Health check to url " + catalogueWebTarget.getUri().toString());

        try {
            Response response = catalogueWebTarget.path("health")
                                                  .request()
                                                  .property(ClientProperties.CONNECT_TIMEOUT, 10000)
                                                  .property(ClientProperties.READ_TIMEOUT, 10000)
                                                  .head();
            if (response.getStatus() >= 200 && response.getStatus() < 300) {
                return builder.up().build();
            }
        } catch (WebApplicationException | ProcessingException e) {
            log.warning(e.getMessage());
        }

        return builder.down().build();
    }
}
