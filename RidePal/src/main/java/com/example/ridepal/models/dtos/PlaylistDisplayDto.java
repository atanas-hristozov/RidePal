package com.example.ridepal.models.dtos;

import com.example.ridepal.models.Track;
import com.example.ridepal.models.User;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.HashSet;
import java.util.Set;

public class PlaylistDisplayDto {
    private String title;
    private double rank;
    private int playlistTime;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String creator;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Set<TrackDisplayDto> tracks;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Set<GenreDisplayDto> genres;

    public PlaylistDisplayDto() {
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

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public Set<TrackDisplayDto> getTracks() {
        return tracks;
    }

    public void setTracks(Set<TrackDisplayDto> tracks) {
        this.tracks = tracks;
    }

    public Set<GenreDisplayDto> getGenres() {
        return genres;
    }

    public void setGenres(Set<GenreDisplayDto> genres) {
        this.genres = genres;
    }

    public void addGenreDisplayDto(GenreDisplayDto genre){
        if(this.genres==null)
            this.genres=new HashSet<>();

        this.genres.add(genre);
    }

    public void addTrackDisplayDto(TrackDisplayDto track){
        if(this.tracks==null)
            this.tracks=new HashSet<>();

        this.tracks.add(track);
    }
}
