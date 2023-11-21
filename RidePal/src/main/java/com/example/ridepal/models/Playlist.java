package com.example.ridepal.models;

import jakarta.persistence.*;

import java.util.Set;

@Entity
@Table(name = "playlists")
@SecondaryTable(name = "playlist_data",
        pkJoinColumns = @PrimaryKeyJoinColumn(name = "user_id"))
public class Playlist {
@Id
@Column(name = "id")
private int id;
@Column(name = "title")
private String title;
@Column(name = "rank")
private double rank;
@Column(name = "playlist_time")
private int playlistTime;
@ManyToOne
@JoinColumn(name = "user_id")
private User user;

    @ManyToMany(cascade = {CascadeType.ALL})
    @JoinTable(
            name = "playlist_data",
            joinColumns = {@JoinColumn(name = "playlist_id")},
            inverseJoinColumns = {@JoinColumn(name = "track_id")}
    )
    private Set<Track> tracks;

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
        this.tracks = tracks;
    }
}
