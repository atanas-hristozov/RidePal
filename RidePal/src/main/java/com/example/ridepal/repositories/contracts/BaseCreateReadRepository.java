package com.example.ridepal.repositories.contracts;

import org.hibernate.Session;

import java.util.List;

public interface BaseCreateReadRepository<T> {

    T getById(int id);
    <V> T getByField(String name, V value);
    public List<T> getAll();
    void create(T entity);
}
