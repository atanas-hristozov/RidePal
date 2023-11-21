package com.example.ridepal.repositories.contracts;

import com.example.ridepal.models.Genre;

public interface GenreRepository {

    public void create(Genre genre);

    Genre getById(int id);
}
