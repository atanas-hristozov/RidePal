package com.example.ridepal.models.dtos;

import com.example.ridepal.models.Genre;
import jakarta.validation.constraints.Size;

import java.util.Set;

public class PlaylistUpdateDto {

    @Size(min = 3, max = 20, message = "Title length should be between 3 and 20")
    private String title;
    private Set<Genre> genres;
    public PlaylistUpdateDto() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Set<Genre> getGenres() {
        return genres;
    }

    public void setGenres(Set<Genre> genres) {
        this.genres = genres;
    }
}
