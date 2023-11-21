package com.example.ridepal.repositories;

import com.example.ridepal.exceptions.EntityNotFoundException;
import com.example.ridepal.repositories.contracts.BaseReadRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.List;

import static java.lang.String.format;

public abstract class AbstractReadRepository<T> implements BaseReadRepository<T> {

    protected final SessionFactory sessionFactory;
    protected final Class<T> clazz;

    public AbstractReadRepository(SessionFactory sessionFactory, Class<T> clazz) {
        this.sessionFactory = sessionFactory;
        this.clazz = clazz;
    }

    @Override
    public List<T> getAll() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery(format("from %s", clazz.getSimpleName()), clazz).list();
        }
    }

    @Override
    public T getById(int id) {
        return getByField("id", id);
    }

    @Override
    public <V> T getByField(String name, V value) {
        String query = String.format("from %s where %s = :value", clazz.getSimpleName(), name);
        String notFoundError = String.format("%s with %s %s not found", clazz.getSimpleName(), name, value);
        try (Session session = sessionFactory.openSession()) {
            return session
                    .createQuery(query, clazz)
                    .setParameter("value", value)
                    .uniqueResultOptional()
                    .orElseThrow(() -> new EntityNotFoundException(notFoundError));
        }
    }
}
