package com.example.ridepal.models;

import java.util.Optional;

public class PlaylistFilterOptions {

    private Optional<Integer> playlistTime;
    private Optional<Genre> genre;

    public PlaylistFilterOptions(int playlistTime, Genre genre) {
        this.playlistTime = Optional.ofNullable(playlistTime);
        this.genre = Optional.ofNullable(genre);
    }

    public Optional<Integer> getPlaylistTime() {
        return playlistTime;
    }

    public Optional<Genre> getGenre() {
        return genre;
    }
}
