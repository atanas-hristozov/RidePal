package com.example.ridepal.controllers;

import com.example.ridepal.exceptions.AuthorizationException;
import com.example.ridepal.exceptions.EntityDuplicateException;
import com.example.ridepal.exceptions.EntityNotFoundException;
import com.example.ridepal.helpers.AuthenticationHelper;
import com.example.ridepal.helpers.PlaylistMapper;
import com.example.ridepal.models.Genre;
import com.example.ridepal.models.Playlist;
import com.example.ridepal.models.User;
import com.example.ridepal.models.dtos.PlaylistDisplayDto;
import com.example.ridepal.models.dtos.PlaylistGenerateDto;
import com.example.ridepal.models.dtos.PlaylistUpdateDto;
import com.example.ridepal.services.contracts.GenreService;
import com.example.ridepal.services.contracts.PlaylistService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/roadbeat/start")
public class PlaylistRestController {

    private final PlaylistService playlistService;
    private final AuthenticationHelper authenticationHelper;
    private final PlaylistMapper playlistMapper;
    private final GenreService genreService;


    @Autowired
    public PlaylistRestController(PlaylistService playlistService,
                                  AuthenticationHelper authenticationHelper,
                                  PlaylistMapper playlistMapper,
                                  GenreService genreService) {
        this.playlistService = playlistService;
        this.authenticationHelper = authenticationHelper;
        this.playlistMapper = playlistMapper;
        this.genreService = genreService;
    }

    @PostMapping("/playlist")
    public void createPlaylist(@RequestHeader HttpHeaders headers,
                               @Valid @RequestBody PlaylistGenerateDto playlistGenerateDto) {
        try {
            User user = authenticationHelper.tryGetUser(headers);
            int travelDuration = playlistGenerateDto.getTravelDuration();
            String genreNames = playlistGenerateDto.getGenreNames();
            Playlist playlist = playlistMapper.fromPlaylistGenerateDto(playlistGenerateDto);

            playlistService.create(user, playlist, travelDuration, genreNames);

        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (EntityDuplicateException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @GetMapping("/playlist/{id}")
    public PlaylistDisplayDto getPlaylist(@PathVariable int id,
                                          @RequestHeader HttpHeaders headers) {
        try {
            User user = authenticationHelper.tryGetUser(headers);
            return playlistMapper.fromPlaylist(playlistService.getById(id));
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (EntityDuplicateException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @GetMapping("/playlists")
    public List<Playlist> getAll() {
        return playlistService.getAll();
    }

    @PutMapping("/playlist/{id}")
    public void updatePlaylist(@PathVariable int id,
                               @RequestHeader HttpHeaders headers,
                               PlaylistUpdateDto playlistUpdateDto) {
        try {
            User user = authenticationHelper.tryGetUser(headers);
            Playlist playlist = playlistMapper.fromPlaylistUpdateDto(playlistUpdateDto, id);
            playlistService.update(user, playlist);

        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (EntityDuplicateException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @DeleteMapping("/playlist/{id}")
    public void deletePlaylist(@PathVariable int id,
                               @RequestHeader HttpHeaders headers) {
        try {
            User user = authenticationHelper.tryGetUser(headers);
            Playlist playlist = playlistService.getById(id);
            playlistService.delete(user, playlist);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (EntityDuplicateException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }
}
