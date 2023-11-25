package com.example.ridepal.models;

import jakarta.persistence.*;

import java.util.Set;

@Entity
@Table(name = "genres")
public class Genre {

    @Id
    @Column(name = "id")
    private int genreId;

    @Column(name = "genre_name")
    private String genreName;
    @ManyToMany(mappedBy = "genres")
    private Set<Playlist> playlist;

    public Genre() {
    }


    public int getGenreId() {
        return genreId;
    }

    public void setGenreId(int genreId) {
        this.genreId = genreId;
    }

    public String getGenreName() {
        return genreName;
    }

    public void setGenreName(String genreName) {
        this.genreName = genreName;
    }

    public Set<Playlist> getPlaylist() {
        return playlist;
    }

    public void setPlaylist(Set<Playlist> playlist) {
        this.playlist = playlist;
    }
}
