package com.example.ridepal.repositories.contracts;

public interface BaseCrudRepository<T> extends BaseReadRepository<T> {

    void delete(int id);
    void create(T entity);
    void update(T entity);
}
