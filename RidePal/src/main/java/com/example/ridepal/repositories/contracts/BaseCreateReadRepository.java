package com.example.ridepal.repositories.contracts;

import org.hibernate.Session;

import java.util.List;

public interface BaseCreateReadRepository<T> {

    T getById(int id);

    <V> T getByField(String name, V value);

    <V> T getByField(String name, V value, Session session);
    public List<T> getAll();

    void create(T entity);
}
