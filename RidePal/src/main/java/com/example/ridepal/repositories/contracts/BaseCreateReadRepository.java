package com.example.ridepal.repositories.contracts;

public interface BaseCreateReadRepository<T> {

    T getById(int id);

    <V> T getByField(String name, V value);

    void create(T entity);
}
