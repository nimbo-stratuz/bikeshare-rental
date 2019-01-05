package si.nimbostratuz.bikeshare.services;

import com.kumuluz.ee.discovery.annotations.DiscoverService;
import com.kumuluz.ee.rest.beans.QueryParameters;
import com.kumuluz.ee.rest.utils.JPAUtils;
import lombok.extern.java.Log;
import si.nimbostratuz.bikeshare.models.dtos.BicycleDTO;
import si.nimbostratuz.bikeshare.models.dtos.PaymentDTO;
import si.nimbostratuz.bikeshare.models.dtos.RentalDTO;
import si.nimbostratuz.bikeshare.models.entities.Rental;
import si.nimbostratuz.bikeshare.services.configuration.AppProperties;
import si.nimbostratuz.bikeshare.services.configuration.Pricing;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import java.math.BigDecimal;
import java.math.MathContext;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Log
@ApplicationScoped
public class RentalsBean extends EntityBean<Rental> {


    @Inject
    // Price of rental per minute in double - make sure to use BigDecimal in operations
    private Pricing pricing;
    @Inject
    private AppProperties appProperties;
    @Inject
    @DiscoverService("bikeshare-catalogue")
    private WebTarget catalogueWebTarget;
    @Inject
    @DiscoverService("bikeshare-payments")
    private WebTarget paymentsWebTarget;

//    @Timed
    public List<Rental> getAll(QueryParameters query) {
        log.info("Current price per minute: " + (pricing.getBigDecimalPricePerMinute()).toString());
        log.info("external services enabled: " + appProperties.isExternalServicesEnabled());
        List<Rental> rentals = JPAUtils.queryEntities(em, Rental.class, query);
        return rentals;
    }

    /**
     *
     * @param query Query Parameter
     * @return  Returns total amount (type: long) of Rentals in the database
     */
    public long getCount(QueryParameters query) {
        Long amount = JPAUtils.queryEntitiesCount(em, Rental.class, query);
        return amount;
    }

    public Rental get(Integer rentalId) {

        Rental rental = em.find(Rental.class, rentalId);

        if (rental == null) {
            throw new NotFoundException();
        }

        return rental;
    }

    @Override
    public Rental create(Rental rental) {

        // Workaround
        rental.setId(null);

        try {
            beginTx();
            em.persist(rental);
            commitTx();
        } catch (Exception e) {
            rollbackTx();
            log.throwing(RentalsBean.class.getName(), "create", e);
            throw new BadRequestException();
        }

        return rental;
    }

    @Override
    public Rental update(Integer id, Rental rental) {

        Rental originalRental = this.get(id);

        try {
            beginTx();
            rental.setId(originalRental.getId());
            rental = em.merge(rental);
            commitTx();
        } catch (Exception e) {
            rollbackTx();
            log.throwing(RentalsBean.class.getName(), "update", e);
        }

        return rental;
    }

    @Override
    public void delete(Integer id) {

        Rental rental = this.get(id);

        try {
            beginTx();
            em.remove(rental);
            commitTx();
        } catch (Exception e) {
            rollbackTx();
            log.throwing(RentalsBean.class.getName(), "delete", e);
        }
    }

    public Rental rentABicycle(RentalDTO rentalDTO) {
        Rental rental = new Rental();

        try {
            beginTx();
            rental.setRentStart(Date.from(Instant.now()));
            rental.setRentEnd(null);
            rental.setEndLocation(null);
            rental.setUserId(rentalDTO.getUserId());

            Integer bicycleId = rentalDTO.getBicycleId();
            // Check if bicycle exists and is free (bikeshare-catalogue)
            // Get targeted Bicycle from bikeshare-catalogue
            BicycleDTO targetedBicycle = catalogueWebTarget.path("v1")
                    .path("bicycles").path(Integer.toString(bicycleId))
                    .request().get().readEntity(BicycleDTO.class);

            log.info(targetedBicycle.toString());
            if (targetedBicycle.getAvailable()) {
                // Make targeted Bicycle unavailable
                targetedBicycle.setAvailable(false);
                rental.setStartLocation(targetedBicycle.getLocation());

                catalogueWebTarget.path("v1")
                        .path("bicycles")
                        .path(Integer.toString(bicycleId))
                        .request().put(Entity.entity(targetedBicycle, MediaType.APPLICATION_JSON));

                rental.setBicycleId(bicycleId);
            } else {
                throw new BadRequestException("Targeted bicycle not available.") ;
            }
            commitTx();
            return this.create(rental);
        } catch (Exception e) {
            log.info(e.toString());
            rollbackTx();
            throw new BadRequestException("Bicycle rental failed.");
        }



    }

    private BigDecimal calculatePrice(Date start, Date end) {
        // Rounded to the minute
        Long durationInMinutes = TimeUnit.MINUTES.convert(
                end.getTime() - start.getTime(),
                TimeUnit.MILLISECONDS);
        durationInMinutes += pricing.getStartMinutes(); // start minutes are charged upon each rent

        log.info("difference in minutes " + durationInMinutes.toString());
        //
        BigDecimal duration = new BigDecimal(durationInMinutes);
        MathContext mc = new MathContext(4);

        return (duration.multiply(pricing.getBigDecimalPricePerMinute(), mc));
    }

    public Rental finalizeRental(Integer rentalId, RentalDTO rentalDTO) {

        Rental rental = this.get(rentalId);
        try {
            beginTx();
            rental.setRentEnd(Date.from(Instant.now()));



            // Get bicycle's location
            BicycleDTO targetedBicycle = catalogueWebTarget.path("v1")
                    .path("bicycles").path(Integer.toString(rentalDTO.getBicycleId()))
                    .request().get().readEntity(BicycleDTO.class);
            if (targetedBicycle.getAvailable()) {
                log.info("Error. The bicycle is not rented.");
                throw new BadRequestException("Bicycle not rented.");
            }
            // Make the targetBicycle available again
            targetedBicycle.setAvailable(true);

            // Make the payment
            BigDecimal totalPrice = calculatePrice(rental.getRentStart(), rental.getRentEnd());
            log.info("Total price for this ride is" + totalPrice.toString());

            PaymentDTO payment = new PaymentDTO();
            // set
            payment.setAmount(totalPrice);
            payment.setFromUserId(rentalDTO.getUserId());
            payment.setToUserId(targetedBicycle.getOwnerId());
            payment.setRideId(rentalId);

            // Post to payments microservice
            try {
                paymentsWebTarget.path("v1").path("payments").request()
                        .post(Entity.entity(payment, MediaType.APPLICATION_JSON));
                log.info("Payment of " + totalPrice.toString() + " € has been made.");

            } catch (Exception e) {
                log.info(e.toString());
                rollbackTx();
                throw new BadRequestException("Payments microservice failed.");
            }

            catalogueWebTarget.path("v1")
                    .path("bicycles")
                    .path(Integer.toString(rentalDTO.getBicycleId()))
                    .request().put(Entity.entity(targetedBicycle, MediaType.APPLICATION_JSON));


            // Set end location on rental
            rental.setEndLocation(targetedBicycle.getLocation());
            commitTx();
            return this.update(rentalId, rental);
        } catch (Exception e) {
            log.info(e.toString());
            rollbackTx();
            throw new BadRequestException("Rental finalization failed.");
        }
    }

}
