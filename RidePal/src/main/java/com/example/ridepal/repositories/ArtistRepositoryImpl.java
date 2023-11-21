package com.example.ridepal.repositories;

import com.example.ridepal.exceptions.EntityNotFoundException;
import com.example.ridepal.models.Artist;


import com.example.ridepal.repositories.contracts.ArtistRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class ArtistRepositoryImpl implements ArtistRepository {

    SessionFactory sessionFactory;

    @Autowired
    public ArtistRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void create(Artist artist) {

        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.persist(artist);
            session.getTransaction().commit();
        }
    }

    @Override
    public Artist getById(int id) {
        try (Session session = sessionFactory.openSession()) {
            Artist artist = session.get(Artist.class, id);
            if (artist == null) {
                throw new EntityNotFoundException("Artist", id);
            }
            return artist;
        }
    }
}
