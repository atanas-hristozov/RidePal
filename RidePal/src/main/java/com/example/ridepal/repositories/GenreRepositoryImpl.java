package com.example.ridepal.repositories;

import com.example.ridepal.models.Genre;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class GenreRepositoryImpl extends AbstractCreateReadRepository<Genre> {
    @Autowired
    public GenreRepositoryImpl(SessionFactory sessionFactory, Class<Genre> clazz) {
        super(sessionFactory, clazz);
    }
}
