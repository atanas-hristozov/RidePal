package com.example.ridepal.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import org.hibernate.engine.internal.Cascade;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "playlists")
public class Playlist {
    @JsonIgnore
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "title")
    private String title;
    @Column(name = "rank")
    private double rank;
    @Column(name = "playlist_time")
    private int playlistTime;
    @ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;
    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.ALL})
    @JoinTable(
            name = "playlist_data",
            joinColumns = {@JoinColumn(name = "playlist_id")},
            inverseJoinColumns = {@JoinColumn(name = "track_id")}
    )
    private Set<Track> tracks;
    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.ALL})
    @JoinTable(
            name = "playlist_genres",
            joinColumns = {@JoinColumn(name = "playlist_id")},
            inverseJoinColumns = {@JoinColumn(name = "genre_id")}
    )
    private Set<Genre> genres;

    public Playlist() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public double getRank() {
        return rank;
    }

    public void setRank(double rank) {
        this.rank = rank;
    }

    public int getPlaylistTime() {
        return playlistTime;
    }

    public void setPlaylistTime(int playlistTime) {
        this.playlistTime = playlistTime;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Set<Track> getTracks() {
        return tracks;
    }
    public void setTracks(Set<Track> tracks) {
        if (this.tracks == null || this.tracks.isEmpty()) {
            this.tracks = tracks;
        }
        this.tracks.addAll(tracks);
        tracks.forEach(track -> track.addPlaylist(this));
    }
    public Set<Genre> getGenres() {
        return genres;
    }

    public void setGenres(Set<Genre> genres) {
        if (this.genres == null || this.genres.isEmpty()) {
            this.genres = genres;
        }
        this.genres.addAll(genres);
        genres.forEach(genre -> genre.addPlaylist(this));
    }
}
