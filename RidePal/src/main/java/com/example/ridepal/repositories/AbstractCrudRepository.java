package com.example.ridepal.repositories;

import com.example.ridepal.repositories.contracts.BaseCrudRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

public abstract class AbstractCrudRepository<T> extends AbstractCreateReadRepository<T> implements BaseCrudRepository<T> {

    protected AbstractCrudRepository(SessionFactory sessionFactory, Class<T> clazz) {
        super(sessionFactory, clazz);
    }

    @Override
    public void delete(int id) {
        T entityToDelete = getByField("id", id);
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.remove(entityToDelete);
            session.getTransaction().commit();
        }
    }

    @Override
    public void update(T entity) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.merge(entity);
            session.getTransaction().commit();
        }
    }
}
