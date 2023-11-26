package com.example.ridepal.models.dtos;

import jakarta.validation.constraints.Size;

public class PlaylistUpdateDto {

    @Size(min = 3, max = 20, message = "Title length should be between 3 and 20")
    private String title;
    private String genreNames;

    public PlaylistUpdateDto() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getGenreNames() {
        return genreNames;
    }

    public void setGenreNames(String genreNames) {
        this.genreNames = genreNames;
    }
}
