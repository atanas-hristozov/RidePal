package com.example.ridepal.services.contracts;

import com.example.ridepal.models.Genre;
import com.example.ridepal.models.User;

import java.util.List;

public interface GenreService {
    void delete (int id);
    Genre getById (int id);
    Genre getByName(String name);
    List<Genre> getAll();
}
