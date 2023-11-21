package com.example.ridepal.repositories;

import com.example.ridepal.exceptions.EntityNotFoundException;
import com.example.ridepal.models.Track;
import com.example.ridepal.repositories.contracts.TrackRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class TrackRepositoryImpl implements TrackRepository {


    SessionFactory sessionFactory;

    @Autowired
    public TrackRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }


    @Override
    public void create(Track track) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.persist(track);
            session.getTransaction().commit();

        }
    }

    @Override
    public Track getById(int id) {
        try (Session session = sessionFactory.openSession()) {
            Track track = session.get(Track.class, id);
            if (track == null) {
                throw new EntityNotFoundException("Track", id);
            }
            return track;
        }
    }
}
