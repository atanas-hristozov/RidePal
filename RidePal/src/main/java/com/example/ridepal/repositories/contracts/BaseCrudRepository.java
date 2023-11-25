package com.example.ridepal.repositories.contracts;

public interface BaseCrudRepository<T> extends BaseCreateReadRepository<T> {
    void delete(int id);
    void update(T entity);
}
