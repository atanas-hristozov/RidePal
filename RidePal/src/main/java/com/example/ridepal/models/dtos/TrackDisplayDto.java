package com.example.ridepal.models.dtos;

import com.example.ridepal.models.Album;
import com.example.ridepal.models.Artist;

public class TrackDisplayDto {

    private String title;

    private int duration;

    private double rank;

    private String previewUrl;

    private ArtistDisplayDto artist;

    private String album;

    public TrackDisplayDto() {
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

    public double getRank() {
        return rank;
    }

    public void setRank(double rank) {
        this.rank = rank;
    }

    public String getPreviewUrl() {
        return previewUrl;
    }

    public void setPreviewUrl(String previewUrl) {
        this.previewUrl = previewUrl;
    }

    public ArtistDisplayDto getArtist() {
        return artist;
    }

    public void setArtist(ArtistDisplayDto artist) {
        this.artist = artist;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }
}
