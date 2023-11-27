package com.example.ridepal.helpers;

import com.example.ridepal.models.Genre;
import com.example.ridepal.models.Playlist;
import com.example.ridepal.models.dtos.PlaylistGenerateDto;
import com.example.ridepal.models.dtos.PlaylistUpdateDto;
import com.example.ridepal.services.contracts.PlaylistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class PlaylistMapper {

    private final PlaylistService playlistService;

    @Autowired
    public PlaylistMapper(PlaylistService playlistService) {
        this.playlistService = playlistService;
    }

    public Playlist fromPlaylistGenerateDto(PlaylistGenerateDto playlistGenerateDto){
        Playlist playlist = new Playlist();
        playlist.setTitle(playlistGenerateDto.getTitle());

        return playlist;
    }

    public Playlist fromPlaylistUpdateDto(PlaylistUpdateDto playlistUpdateDto, int id){
        Playlist playlist = playlistService.getById(id);
        playlist.setTitle(playlistUpdateDto.getTitle());

        return playlist;
    }
}
