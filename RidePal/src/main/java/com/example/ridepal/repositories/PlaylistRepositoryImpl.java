package com.example.ridepal.repositories;

import com.example.ridepal.models.Playlist;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class PlaylistRepositoryImpl extends AbstractCrudRepository<Playlist> {

    @Autowired
    public PlaylistRepositoryImpl(SessionFactory sessionFactory, Class<Playlist> clazz) {
        super(sessionFactory, clazz);
    }
}
