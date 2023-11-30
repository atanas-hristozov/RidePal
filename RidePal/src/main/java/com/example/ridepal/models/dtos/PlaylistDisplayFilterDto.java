package com.example.ridepal.models.dtos;

public class PlaylistDisplayFilterDto {

    private String title;
    private int playlistTimeFrom;
    private int playlistTimeTo;
    private String genreName;

    public PlaylistDisplayFilterDto() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getPlaylistTimeFrom() {
        return playlistTimeFrom;
    }

    public void setPlaylistTimeFrom(int playlistTimeFrom) {
        this.playlistTimeFrom = playlistTimeFrom;
    }

    public int getPlaylistTimeTo() {
        return playlistTimeTo;
    }

    public void setPlaylistTimeTo(int playlistTimeTo) {
        this.playlistTimeTo = playlistTimeTo;
    }

    public String getGenreName() {
        return genreName;
    }

    public void setGenreName(String genreName) {
        this.genreName = genreName;
    }
}
