package com.example.ridepal.helpers;

import com.example.ridepal.models.Genre;
import com.example.ridepal.models.Playlist;
import com.example.ridepal.models.PlaylistFilterOptions;
import com.example.ridepal.models.Track;
import com.example.ridepal.models.dtos.*;
import com.example.ridepal.services.contracts.PlaylistService;
import org.jetbrains.annotations.NotNull;
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

    public Playlist fromPlaylistGenerateDto(PlaylistGenerateDto playlistGenerateDto) {
        Playlist playlist = new Playlist();
        playlist.setTitle(playlistGenerateDto.getTitle());

        return playlist;
    }

    public Playlist fromPlaylistUpdateDto(PlaylistUpdateDto playlistUpdateDto, int id) {
        Playlist playlist = playlistService.getById(id);
        playlist.setTitle(playlistUpdateDto.getTitle());
      //  playlistUpdateDto.getGenres().forEach(playlist::addGenre);

        return playlist;
    }
    public PlaylistUpdateDto fromPlaylistToPlaylistUpdateDto(Playlist playlist) {
        PlaylistUpdateDto playlistUpdateDto = new PlaylistUpdateDto();
        playlistUpdateDto.setTitle(playlist.getTitle());
        playlistUpdateDto.setGenres(playlist.getGenres());

        return playlistUpdateDto;
    }


    public PlaylistDisplayDto fromPlaylist(Playlist playlist) {
        PlaylistDisplayDto playlistDto = new PlaylistDisplayDto();
        playlistDto.setTitle(playlist.getTitle());
        playlistDto.setCreator(playlist.getUser().getUsername());
        playlistDto.setPlaylistTime(playlist.getPlaylistTime());
        playlistDto.setRank(playlist.getRank());
        for (Genre genre :
                playlist.getGenres()) {
            GenreDisplayDto genreDto = new GenreDisplayDto();
            genreDto.setName(genre.getGenreName());
            playlistDto.addGenreDisplayDto(genreDto);
        }
        for (Track track :
                playlist.getTracks()) {
            TrackDisplayDto trackDto = getTrackDisplayDto(track);
            playlistDto.addTrackDisplayDto(trackDto);
        }
        return playlistDto;
    }

    private static TrackDisplayDto getTrackDisplayDto(Track track) {
        TrackDisplayDto trackDto = new TrackDisplayDto();
        trackDto.setTitle(track.getTitle());
        trackDto.setAlbum(track.getAlbum() != null ? track.getAlbum().getAlbumName() : "N/A");
        trackDto.setDuration(track.getDuration());
        trackDto.setPreviewUrl(track.getPreviewUrl());
        trackDto.setRank(track.getRank());
        ArtistDisplayDto artistDto = new ArtistDisplayDto();
        artistDto.setName(track.getArtist().getArtistName());
        artistDto.setPhoto(track.getArtist().getArtistPhoto());
        trackDto.setArtist(artistDto);
        return trackDto;
    }
}
