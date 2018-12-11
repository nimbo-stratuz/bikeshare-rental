package si.nimbostratuz.bikeshare.api.v1.resources;

import com.kumuluz.ee.rest.beans.QueryParameters;
import si.nimbostratuz.bikeshare.models.dtos.RentalDTO;
import si.nimbostratuz.bikeshare.models.entities.Rental;
import si.nimbostratuz.bikeshare.services.RentalsBean;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.List;

@ApplicationScoped
@Path("rentals")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class RentalsResource {

    @Inject
    private RentalsBean rentalsBean;
    @Context
    protected UriInfo uriInfo;

    @GET
    public Response getRentals() {
        QueryParameters query = QueryParameters.query(uriInfo.getRequestUri().getQuery()).build();

        List<Rental> rentals = rentalsBean.getAll(query);
        Long rentalsAmount = rentalsBean.getCount(query);

        return Response.ok(rentals).header("X-Total-Count", rentalsAmount).build();
    }

    @GET
    @Path("{id}")
    public Response getRental(@PathParam("id") Integer id) {

        return Response.ok(rentalsBean.get(id)).build();
    }

    @POST
    /*
        Doesn't get called. Call "/rent" instead. Would do the same thing.
     */
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


    @POST
    @Path("rent")
    /*
        Called when rental is initialised. Makes the bicycle unavailable etc.
     */
    public Response rentABicycle(RentalDTO rentalDTO) {
        return Response.ok(rentalsBean.rentABicycle(rentalDTO)).build();
    }

    @POST
    @Path("{id}/finalize")
    /*
        Gets called when a rental has ended. Makes the bicycle available, sets
        ending location etc.
     */
    public Response finalizeRent(@PathParam("id") Integer rentalId, RentalDTO rentalDTO) {
        return Response.ok(rentalsBean.finalizeRental(rentalId, rentalDTO)).build();

    }

}
