package si.nimbostratuz.bikeshare.services;

import com.kumuluz.ee.rest.beans.QueryParameters;
import com.kumuluz.ee.rest.utils.JPAUtils;
import lombok.extern.java.Log;
import si.nimbostratuz.bikeshare.models.dtos.RentalDTO;
import si.nimbostratuz.bikeshare.models.entities.Rental;
import si.nimbostratuz.bikeshare.services.configuration.AppProperties;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.TypedQuery;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.NotFoundException;
import java.time.Instant;
import java.util.Date;
import java.util.List;

@Log
@ApplicationScoped
public class RentalsBean extends EntityBean<Rental> {


    @Inject
    private AppProperties appProperties;

    public List<Rental> getAll(QueryParameters query) {
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

        rental.setRentStart(Date.from(Instant.now()));
        rental.setRentEnd(null);

        Integer bicycleId = rentalDTO.getBicycleId();
        // TODO: Check if bicycle exists and is free (bikeshare-catalogue)
        rental.setBicycleId(bicycleId);

        // TODO: use bicycle's location
        rental.setStartLocation(rentalDTO.getStartLocation());
        rental.setEndLocation(null);

        return this.create(rental);
    }

    public Rental finalizeRental(Integer rentalId, RentalDTO rentalDTO) {

        Rental rental = this.get(rentalId);

        rental.setRentEnd(Date.from(Instant.now()));

        // TODO: Get bicycle's location
        rental.setEndLocation(rentalDTO.getEndLocation());

        return this.update(rentalId, rental);
    }

}
