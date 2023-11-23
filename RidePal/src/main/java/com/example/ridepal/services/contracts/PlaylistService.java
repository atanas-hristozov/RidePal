package com.example.ridepal.services.contracts;

import com.example.ridepal.models.Playlist;

public interface PlaylistService {

    Playlist getById(int id);
    void create(Playlist playlist);
    void update(Playlist playlist);
    void delete(int id);
}
