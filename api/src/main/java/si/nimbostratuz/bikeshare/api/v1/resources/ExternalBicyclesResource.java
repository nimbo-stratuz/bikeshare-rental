package si.nimbostratuz.bikeshare.api.v1.resources;

import si.nimbostratuz.bikeshare.services.BicyclesService;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@ApplicationScoped
@Path("external/bicycles")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ExternalBicyclesResource {

    @Inject
    private BicyclesService bicyclesService;

    @GET
    public Response getBicycles() {

        return Response.ok(bicyclesService.getAll()).build();
    }

    @GET
    @Path("{id}")
    public Response getBicycle(@PathParam("id") Integer id) {

        return Response.ok(bicyclesService.getBicycle(id)).build();
    }
}
