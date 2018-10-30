package si.nimbostratuz.bikeshare.api.v1.resources;

import si.nimbostratuz.bikeshare.models.entities.Rental;
import si.nimbostratuz.bikeshare.services.RentalsBean;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@ApplicationScoped
@Path("rentals")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class RentalsResource {

    @Inject
    private RentalsBean rentalsBean;

    @GET
    public Response getRentals() {

        return Response.ok(rentalsBean.getAll()).build();
    }

    @GET
    @Path("{id}")
    public Response getRental(@PathParam("id") Integer id) {

        return Response.ok(rentalsBean.get(id)).build();
    }

    @POST
    public Response createRental(Rental rental) {

        return Response.ok(rentalsBean.create(rental)).build();
    }

    @PUT
    @Path("{id}")
    public Response updateRental(@PathParam("id") Integer id, Rental rental) {

        return Response.ok(rentalsBean.update(id, rental)).build();
    }

    @DELETE
    @Path("{id}")
    public Response deleteRental(@PathParam("id") Integer id) {

        rentalsBean.delete(id);

        return Response.status(Response.Status.NO_CONTENT).build();
    }
}