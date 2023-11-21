package com.example.ridepal.repositories;

import com.example.ridepal.exceptions.EntityNotFoundException;
import com.example.ridepal.models.Album;
import com.example.ridepal.repositories.contracts.AlbumRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class AlbumRepositoryImpl implements AlbumRepository {

    SessionFactory sessionFactory;
@Autowired
    public AlbumRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }


    @Override
    public void create(Album album) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.persist(album);
            session.getTransaction().commit();
        }
    }

    @Override
    public Album getById(int id) {
        try (Session session = sessionFactory.openSession()) {
            Album album = session.get(Album.class, id);
            if (album == null) {
                throw new EntityNotFoundException("Album", id);
            }
            return album;
        }
    }
}
