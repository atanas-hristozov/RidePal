package com.example.ridepal.models;

import jakarta.persistence.*;

import java.util.Set;

@Entity
@Table(name = "tracks")
public class Track {
    @Id
    @Column(name = "id")
    private int trackId;
    @Column(name = "title")
    private String title;
    @Column(name = "duration")
    private int duration;
    @Column(name = "rank")
    private double rank;
    @Column(name = "preview_url")
    private String previewUrl;
    @ManyToOne
    @JoinColumn(name = "artist_id")
    private Artist artist;
    @ManyToOne
    @JoinColumn(name = "album_id")
    private Album album;

    @ManyToOne
    @JoinColumn(name = "genre_id")
    private Genre genre;

    /*@ManyToMany(mappedBy = "tracks", cascade = CascadeType.ALL)
    private Set<Playlist> playlists;*/

    public Track() {
    }

    public int getTrackId() {
        return trackId;
    }

    public void setTrackId(int trackId) {
        this.trackId = trackId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public Artist getArtist() {
        return artist;
    }

    public void setArtist(Artist artist) {
        this.artist = artist;
    }

    public Album getAlbum() {
        return album;
    }

    public void setAlbum(Album album) {
        this.album = album;
    }

    public String getPreviewUrl() {
        return previewUrl;
    }

    public void setPreviewUrl(String previewUrl) {
        this.previewUrl = previewUrl;
    }

    public double getRank() {
        return rank;
    }

    public void setRank(double rank) {
        this.rank = rank;
    }

    public Genre getGenre() {
        return genre;
    }

    public void setGenre(Genre genre) {
        this.genre = genre;
    }
}
