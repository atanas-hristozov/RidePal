package com.example.ridepal.models;

import jakarta.persistence.*;

import java.util.Set;

@Entity
@Table(name = "playlists")
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
@ManyToMany(mappedBy = "Playlists")
private Set<User> users;

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

    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }
}
