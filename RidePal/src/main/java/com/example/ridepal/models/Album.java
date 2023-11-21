package com.example.ridepal.models;

import jakarta.persistence.*;

@Entity
@Table(name = "albums")
public class Album {
    @Id
    @Column(name = "id")
    private int albumId;
    @Column(name = "title")
    private String albumName;
    @Column(name = "tracklist_url")
    private String tracklistUrl;
    @OneToOne
    @JoinColumn(name = "genre_id")
    private Genre genre;

    public Album() {
    }

    public int getAlbumId() {
        return albumId;
    }

    public void setAlbumId(int albumId) {
        this.albumId = albumId;
    }

    public String getAlbumName() {
        return albumName;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

    public String getTracklistUrl() {
        return tracklistUrl;
    }

    public void setTracklistUrl(String tracklistUrl) {
        this.tracklistUrl = tracklistUrl;
    }

    public Genre getGenre() {
        return genre;
    }

    public void setGenre(Genre genre) {
        this.genre = genre;
    }
}
