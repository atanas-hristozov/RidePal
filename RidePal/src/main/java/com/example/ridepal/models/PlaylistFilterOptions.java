package com.example.ridepal.models;

import java.util.Optional;

public class PlaylistFilterOptions {
    private Optional<String> title;
    private Optional<Integer> playlistTime;
    private Optional<String> genreName;

    public PlaylistFilterOptions(String title, Integer playlistTime, String genreName) {
        this.title = Optional.ofNullable(title);
        this.playlistTime = Optional.ofNullable(playlistTime);
        this.genreName = Optional.ofNullable(genreName);
    }

    public Optional<Integer> getPlaylistTime() {
        return playlistTime;
    }

    public Optional<String> getGenreName() {
        return genreName;
    }

    public Optional<String> getTitle() {
        return title;
    }
}
