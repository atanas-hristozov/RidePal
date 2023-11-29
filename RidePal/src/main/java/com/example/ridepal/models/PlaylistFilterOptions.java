package com.example.ridepal.models;

import java.util.Optional;

public class PlaylistFilterOptions {
    private Optional<String> title;
    private Optional<Integer> playlistTimeFrom;
    private Optional<Integer> playlistTimeTo;
    private Optional<String> genreName;

    public PlaylistFilterOptions(String title, Integer playlistTimeFrom, Integer playlistTimeTo, String genreName) {
        this.title = Optional.ofNullable(title);
        this.playlistTimeFrom = Optional.ofNullable(playlistTimeFrom);
        this.playlistTimeTo=Optional.ofNullable(playlistTimeTo);
        this.genreName = Optional.ofNullable(genreName);
    }

    public Optional<Integer> getPlaylistTimeFrom() {
        return playlistTimeFrom;
    }

    public Optional<Integer> getPlaylistTimeTo() {
        return playlistTimeTo;
    }

    public Optional<String> getGenreName() {
        return genreName;
    }

    public Optional<String> getTitle() {
        return title;
    }
}
