package com.example.ridepal.services.contracts;

import com.example.ridepal.models.Genre;
import com.example.ridepal.models.Playlist;
import com.example.ridepal.models.User;

import java.util.List;

public interface PlaylistService {

    Playlist getById(int id);
    void create(User user, Playlist playlist, int travelDuration);

    void update(Playlist playlist);
    void delete(int id);
}
