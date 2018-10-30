package si.nimbostratuz.bikeshare.services;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.List;


public abstract class EntityBean<T> {

    @Inject
    protected EntityManager em;

    public abstract List<T> getAll();

    public abstract T get(Integer id);

    public abstract T create(T entity);

    public abstract T update(Integer id, T entity);

    public abstract void delete(Integer id);

    protected void beginTx() {
        if (!em.getTransaction().isActive())
            em.getTransaction().begin();
    }

    protected void commitTx() {
        if (em.getTransaction().isActive())
            em.getTransaction().commit();
    }

    protected void rollbackTx() {
        if (em.getTransaction().isActive())
            em.getTransaction().rollback();
    }
}
