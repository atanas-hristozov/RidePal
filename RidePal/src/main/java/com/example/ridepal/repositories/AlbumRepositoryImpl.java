package com.example.ridepal.repositories;

import com.example.ridepal.models.Album;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class AlbumRepositoryImpl extends AbstractCreateReadRepository<Album> {
    @Autowired
    public AlbumRepositoryImpl(SessionFactory sessionFactory, Class<Album> clazz) {
        super(sessionFactory, clazz);
    }
}
