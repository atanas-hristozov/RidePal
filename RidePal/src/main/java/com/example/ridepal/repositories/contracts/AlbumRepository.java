package com.example.ridepal.repositories.contracts;

import com.example.ridepal.models.Album;

public interface AlbumRepository {

    public void create (Album album);
    Album getById (int id);
}
