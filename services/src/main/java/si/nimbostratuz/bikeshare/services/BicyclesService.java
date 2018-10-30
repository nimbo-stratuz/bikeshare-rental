package si.nimbostratuz.bikeshare.services;

import lombok.extern.java.Log;
import si.nimbostratuz.bikeshare.models.dtos.BicycleDTO;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.ProcessingException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.GenericType;

@Log
@ApplicationScoped
public class BicyclesService {

    private Client httpClient;

    // @Inject
    // @DiscoverService("bikeshare-catalogue")
    // private Optional<String> catalogueBaseUrl;

    private String catalogueBaseUrl = "http://localhost:8080";

    private void init() {
        this.httpClient = ClientBuilder.newClient();
    }

    public BicycleDTO getBicycle(Integer id) {
        try {
            return httpClient.target(catalogueBaseUrl + "/v1/bicycles/" + id.toString())
                             .request().get(new GenericType<BicycleDTO>() {
                    });
        } catch (WebApplicationException | ProcessingException e) {
            log.severe(e.getMessage());
            throw new InternalServerErrorException();
        }
    }
}
