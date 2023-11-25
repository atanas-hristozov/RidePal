package com.example.ridepal.services;

import com.example.ridepal.models.Genre;
import com.example.ridepal.models.User;
import com.example.ridepal.repositories.AbstractCrudRepository;
import com.example.ridepal.services.contracts.GenreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GenreServiceImpl implements GenreService {

    private final AbstractCrudRepository<Genre> genreAbstractCrudRepository;

    @Autowired
    public GenreServiceImpl(AbstractCrudRepository<Genre> genreAbstractCrudRepository) {
        this.genreAbstractCrudRepository = genreAbstractCrudRepository;
    }

    @Override
    public void delete(int id) {
        genreAbstractCrudRepository.delete(id);
    }

    @Override
    public Genre getById(int id) {
        return genreAbstractCrudRepository.getById(id);
    }

    @Override
    public Genre getByName(String name) {
        return genreAbstractCrudRepository.getByField("genreName", name);
    }

    @Override
    public List<Genre> getAll() {
        return genreAbstractCrudRepository.getAll();
    }
}
