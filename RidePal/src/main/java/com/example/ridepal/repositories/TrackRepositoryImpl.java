package com.example.ridepal.repositories;

import com.example.ridepal.models.Track;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class TrackRepositoryImpl extends AbstractCreateReadRepository<Track> {
    @Autowired
    public TrackRepositoryImpl(SessionFactory sessionFactory, Class<Track> clazz) {
        super(sessionFactory, clazz);
    }
}
