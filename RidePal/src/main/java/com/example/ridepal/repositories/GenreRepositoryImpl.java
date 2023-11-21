package com.example.ridepal.repositories;

import com.example.ridepal.exceptions.EntityNotFoundException;
import com.example.ridepal.models.Genre;
import com.example.ridepal.repositories.contracts.GenreRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class GenreRepositoryImpl implements GenreRepository {

    SessionFactory sessionFactory;

    @Autowired
    public GenreRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }


    @Override
    public void create(Genre genre) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.persist(genre);
            session.getTransaction().commit();

        }
    }

    @Override
    public Genre getById(int id) {
        try (Session session = sessionFactory.openSession()) {
            Genre genre = session.get(Genre.class, id);
            if (genre == null) {
                throw new EntityNotFoundException("Genre", id);
            }
            return genre;
        }
    }
}
