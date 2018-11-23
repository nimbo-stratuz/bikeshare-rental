package si.nimbostratuz.bikeshare.services;

import com.kumuluz.ee.discovery.annotations.DiscoverService;
import lombok.extern.java.Log;
import si.nimbostratuz.bikeshare.models.dtos.BicycleDTO;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.ProcessingException;
import javax.ws.rs.ServiceUnavailableException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import java.util.List;

@Log
@ApplicationScoped
public class BicyclesService {

    @Inject
    @DiscoverService("bikeshare-catalogue")
    private WebTarget catalogueWebTarget;

    public List<BicycleDTO> getAll() {
        try {
            return catalogueWebTarget.path("v1")
                                     .path("bicycles")
                                     .request().get(new GenericType<List<BicycleDTO>>() {});

        } catch (WebApplicationException | ProcessingException e) {
            log.severe(e.getMessage());
            throw new ServiceUnavailableException("bikeshare-catalogue not available");
        }
    }

    public BicycleDTO getBicycle(Integer id) {
        try {
            return catalogueWebTarget.path("v1")
                                     .path("bicycles")
                                     .path(id.toString())
                                     .request().get(new GenericType<BicycleDTO>() {});

        } catch (WebApplicationException | ProcessingException e) {
            log.severe(e.getMessage());
            throw new ServiceUnavailableException("bikeshare-catalogue not available");
        }
    }
}
