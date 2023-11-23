package com.example.ridepal.repositories;

import com.example.ridepal.models.Artist;


import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class ArtistRepositoryImpl extends AbstractCreateReadRepository<Artist> {
    @Autowired
    public ArtistRepositoryImpl(SessionFactory sessionFactory, Class<Artist> clazz) {
        super(sessionFactory, clazz);
    }
}
