package com.example.ridepal.models.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.HashSet;
import java.util.Set;

public class UserDisplayDto {

    private String firstName;
    private String lastName;
    private String username;
    private String email;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String userPhoto;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Set<PlaylistDisplayDto> playlists;

    public UserDisplayDto() {
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserPhoto() {
        return userPhoto;
    }

    public void setUserPhoto(String userPhoto) {
        this.userPhoto = userPhoto;
    }

    public Set<PlaylistDisplayDto> getPlaylists() {
        return playlists;
    }

    public void addPlaylist(PlaylistDisplayDto playlistDisplayDto) {
        if(this.playlists == null)
            this.playlists = new HashSet<>();

        this.playlists.add(playlistDisplayDto);
    }
}
