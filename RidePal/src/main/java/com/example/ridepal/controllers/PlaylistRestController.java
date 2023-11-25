package com.example.ridepal.controllers;

import com.example.ridepal.exceptions.AuthorizationException;
import com.example.ridepal.exceptions.EntityDuplicateException;
import com.example.ridepal.exceptions.EntityNotFoundException;
import com.example.ridepal.helpers.AuthenticationHelper;
import com.example.ridepal.helpers.PlaylistMapper;
import com.example.ridepal.models.Genre;
import com.example.ridepal.models.Playlist;
import com.example.ridepal.models.User;
import com.example.ridepal.models.dtos.PlaylistGenerateDto;
import com.example.ridepal.services.contracts.GenreService;
import com.example.ridepal.services.contracts.PlaylistService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/roadbeat/start")
public class PlaylistRestController {

    private final PlaylistService playlistService;
    private final AuthenticationHelper authenticationHelper;
    private final PlaylistMapper playlistMapper;


    @Autowired
    public PlaylistRestController(PlaylistService playlistService,
                                  AuthenticationHelper authenticationHelper,
                                  PlaylistMapper playlistMapper) {
        this.playlistService = playlistService;
        this.authenticationHelper = authenticationHelper;
        this.playlistMapper = playlistMapper;
    }

    @PostMapping("/playlist")
    public void createPlaylist(@RequestHeader HttpHeaders headers,
                               @Valid @RequestBody PlaylistGenerateDto playlistGenerateDto) {
        try {
            User user = authenticationHelper.tryGetUser(headers);
            int travelDuration = playlistGenerateDto.getTravelDuration();
            Playlist playlist = playlistMapper.fromPlaylistGenerateDto(playlistGenerateDto);

            playlistService.create(user, playlist, travelDuration);

        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (EntityDuplicateException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }

    }

}
