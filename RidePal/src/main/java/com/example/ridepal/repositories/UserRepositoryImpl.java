package com.example.ridepal.repositories;

import com.example.ridepal.models.User;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepositoryImpl extends AbstractCrudRepository<User>{
    @Autowired
    public UserRepositoryImpl(SessionFactory sessionFactory, Class<User> clazz) {
        super(sessionFactory, clazz);
    }
}
