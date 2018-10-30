package si.nimbostratuz.bikeshare.services;

import lombok.extern.java.Log;
import si.nimbostratuz.bikeshare.models.entities.Bicycle;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.TypedQuery;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.NotFoundException;
import java.time.Instant;
import java.util.Date;
import java.util.List;

@Log
@ApplicationScoped
public class BicyclesBean extends EntityBean<Bicycle> {

    public List<Bicycle> getAll() {

        TypedQuery<Bicycle> query = em.createNamedQuery("Bicycle.getAll", Bicycle.class);

        return query.getResultList();
    }

    public Bicycle get(Integer bicycleId) {

        Bicycle bicycle = em.find(Bicycle.class, bicycleId);

        if (bicycle == null) {
            throw new NotFoundException();
        }

        return bicycle;
    }

    public Bicycle getBySmartLockUUID(String smartLockUUID) {

        TypedQuery<Bicycle> query = em.createNamedQuery("Bicycle.findBySmartLockUUID", Bicycle.class);
        query.setParameter("smartLockUUID", smartLockUUID);

        List<Bicycle> resultList = query.getResultList();

        if (!resultList.isEmpty()) {
            return resultList.get(0);
        } else {
            throw new NotFoundException();
        }
    }

    @Override
    public Bicycle create(Bicycle bicycle) {

        // Workaround
        bicycle.setId(null);

        try {
            beginTx();

            bicycle.setDateAdded(Date.from(Instant.now()));
            bicycle.setAvailable(false);

            em.persist(bicycle);

            commitTx();
        } catch (Exception e) {
            rollbackTx();
            log.throwing(BicyclesBean.class.getName(), "create", e);
            throw new BadRequestException();
        }

        return bicycle;
    }

    @Override
    public Bicycle update(Integer id, Bicycle bicycle) {

        Bicycle originalBicycle = this.get(id);

        try {
            beginTx();
            bicycle.setId(originalBicycle.getId());
            bicycle = em.merge(bicycle);
            commitTx();
        } catch (Exception e) {
            rollbackTx();
            log.throwing(BicyclesBean.class.getName(), "update", e);
        }

        return bicycle;
    }

    @Override
    public void delete(Integer id) {

        Bicycle bicycle = this.get(id);

        try {
            beginTx();
            em.remove(bicycle);
            commitTx();
        } catch (Exception e) {
            rollbackTx();
            log.throwing(BicyclesBean.class.getName(), "delete", e);
        }
    }

}
