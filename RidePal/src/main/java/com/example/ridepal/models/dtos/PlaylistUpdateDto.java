package com.example.ridepal.models.dtos;

import com.example.ridepal.models.Genre;
import jakarta.validation.constraints.Size;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class PlaylistUpdateDto {

/*    @Size(min = 3, max = 20, message = "Title length should be between 3 and 20")*/
    private String title;
    private String genres;
    public PlaylistUpdateDto() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getGenres() {
        return genres;
    }

    public void setGenres(String genres) {
        this.genres = genres;
    }

}
