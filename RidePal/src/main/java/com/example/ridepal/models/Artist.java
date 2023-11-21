package com.example.ridepal.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;


@Entity
@Table(name = "artists")
public class Artist {
    @Id
    @Column(name = "id")
    private int artistId;
    @Column(name = "artist_name")
    private String artistName;
    @Column(name = "artist_photo")
    private String artistPhoto;

    @Column(name = "artist_tracklist")
    private String artistTracklist;

    public Artist() {
    }

    public int getArtistId() {
        return artistId;
    }

    public void setArtistId(int artistId) {
        this.artistId = artistId;
    }

    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    public String getArtistPhoto() {
        return artistPhoto;
    }

    public void setArtistPhoto(String artistPhoto) {
        this.artistPhoto = artistPhoto;
    }


    public String getArtistTracklist() {
        return artistTracklist;
    }

    public void setArtistTracklist(String artistTracklist) {
        this.artistTracklist = artistTracklist;
    }

}
