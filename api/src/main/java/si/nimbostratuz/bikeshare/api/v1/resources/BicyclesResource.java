package si.nimbostratuz.bikeshare.api.v1.resources;

import si.nimbostratuz.bikeshare.models.entities.Bicycle;
import si.nimbostratuz.bikeshare.services.BicyclesBean;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@ApplicationScoped
@Path("bicycles")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class BicyclesResource {

    @Inject
    private BicyclesBean bicyclesBean;

    @GET
    public Response getBicycles() {

        return Response.ok(bicyclesBean.getAll()).build();
    }

    @GET
    @Path("{id}")
    public Response getBicycles(@PathParam("id") Integer id) {

        return Response.ok(bicyclesBean.get(id)).build();
    }

    @POST
    public Response createBicycle(Bicycle bicycle) {

        return Response.ok(bicyclesBean.create(bicycle)).build();
    }

    @PUT
    @Path("{id}")
    public Response updateBicycle(@PathParam("id") Integer id, Bicycle bicycle) {

        return Response.ok(bicyclesBean.update(id, bicycle)).build();
    }

    @DELETE
    @Path("{id}")
    public Response deleteBicycle(@PathParam("id") Integer id) {

        bicyclesBean.delete(id);

        return Response.status(Response.Status.NO_CONTENT).build();
    }
}
