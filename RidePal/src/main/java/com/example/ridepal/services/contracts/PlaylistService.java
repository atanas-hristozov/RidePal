package com.example.ridepal.services.contracts;

import com.example.ridepal.models.Genre;
import com.example.ridepal.models.Playlist;
import com.example.ridepal.models.PlaylistFilterOptions;
import com.example.ridepal.models.User;

import java.util.List;

public interface PlaylistService {

    Playlist getById(int id);
    List<Playlist> getAll(PlaylistFilterOptions playlistFilterOptions);
    List<Playlist> getAllByCreator(int creatorId);
    Long allPlaylistsCount();
    void create(User user, Playlist playlist, int travelDuration, String genreNames);
    void update(User user, Playlist playlist);
    void delete(User user, Playlist playlist);
    Long tracksNumberPerGenre(int genreId);
    Long allTracksNumber();
}
