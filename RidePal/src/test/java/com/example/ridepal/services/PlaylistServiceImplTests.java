package com.example.ridepal.services;

import com.example.ridepal.PlaylistDataHelpers;
import com.example.ridepal.UserHelpers;
import com.example.ridepal.exceptions.AuthorizationException;
import com.example.ridepal.models.Genre;
import com.example.ridepal.models.Playlist;
import com.example.ridepal.models.PlaylistFilterOptions;
import com.example.ridepal.models.User;
import com.example.ridepal.repositories.contracts.BaseCrudRepository;
import com.example.ridepal.repositories.contracts.PlaylistRepository;
import com.example.ridepal.repositories.contracts.TrackRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PlaylistServiceImplTests {
    @Mock
    PlaylistRepository playlistRepository;
    @Mock
    BaseCrudRepository<Genre> genreBaseCrudRepository;
    @Mock
    TrackRepository trackRepository;
    @InjectMocks
    PlaylistServiceImpl playlistService;

    @Test
    void getById_ShouldReturnPlaylistFromRepository() {
        // Arrange
        Playlist expectedPlaylist = PlaylistDataHelpers.createPlaylist();
        int expectedPlayListId = expectedPlaylist.getId();
        when(playlistRepository.getById(expectedPlaylist.getId())).thenReturn(expectedPlaylist);
        // Act
        Playlist actualPlaylist = playlistService.getById(expectedPlayListId);

        assertEquals(expectedPlaylist.getId(), actualPlaylist.getId());
    }

    @Test
    void getAll_ShouldReturnListOfPlaylistsFromRepository() {
        // Arrange
        PlaylistFilterOptions filterOptions = new PlaylistFilterOptions(null, null, null, null);
        List<Playlist> expectedPlaylists = Arrays.asList(new Playlist(), new Playlist());
        when(playlistRepository.getAllByFilterOptions(filterOptions)).thenReturn(expectedPlaylists);
        // Act
        List<Playlist> actualPlaylists = playlistService.getAll(filterOptions);
        // Assert
        assertEquals(expectedPlaylists, actualPlaylists);
    }

 /*   @Test
    void create_ShouldCreatePlaylistWithTracks() {
        // Arrange
        User user = UserHelpers.createMockUser();
        Playlist playlist = new Playlist();
        Track track = PlaylistDataHelpers.createTrack();
        int travelDuration = 210;
        String genreNames = "Rock";
        when(genreBaseCrudRepository.getByField("genreName", "Rock")).thenReturn(new Genre());

        // Use a unique track for the mock setup

        // Act
        playlistService.create(user, playlist, travelDuration, genreNames);

        // Assert
        assertNotNull(playlist.getTracks());
        assertFalse(playlist.getTracks().isEmpty());
        assertEquals(1, playlist.getTracks().size()); // Assuming one unique track is added
    }*/

    @Test
    void update_ShouldUpdatePlaylist() {
        User executingUser = UserHelpers.createMockUser();
        Playlist playlist = PlaylistDataHelpers.createPlaylist();
        playlist.setUser(executingUser); // Assuming a different user created the playlist
        // Act
        playlistService.update(executingUser, playlist);
        // Assert
        verify(playlistRepository, times(1)).update(playlist);
    }

    @Test
    void update_AdminShouldUpdatePlaylist() {
        // Arrange
        User executingUser = UserHelpers.createMockUser();
        User admin = UserHelpers.createAdmin();
        Playlist playlist = PlaylistDataHelpers.createPlaylist();
        playlist.setUser(executingUser);
        // Act
        playlistService.update(admin, playlist);
        // Assert
        verify(playlistRepository, times(1)).update(playlist);
    }

    @Test
    void update_ShouldThrowAuthorizationExceptionForNonAdminUsers() {
        // Arrange
        User executingUser = UserHelpers.createMockUser();
        User differentUser = UserHelpers.createDifferentuser();
        Playlist playlist = PlaylistDataHelpers.createPlaylist();
        playlist.setUser(executingUser); // Assuming a different user created the playlist
        // Act and Assert
        assertThrows(AuthorizationException.class, () -> playlistService.update(differentUser, playlist));
    }

    @Test
    void delete_ShouldDeletePlaylist() {
        // Arrange
        User executingUser = UserHelpers.createMockUser();
        Playlist playlist = PlaylistDataHelpers.createPlaylist();
        playlist.setUser(executingUser); // Assuming the same user created the playlist
        // Act
        playlistService.delete(executingUser, playlist);
        // Assert
        verify(playlistRepository, times(1)).delete(playlist.getId());
    }

    @Test
    void delete_AdminShouldDeletePlaylist() {
        // Arrange
        User executingUser = UserHelpers.createMockUser();
        User admin = UserHelpers.createAdmin();
        Playlist playlist = PlaylistDataHelpers.createPlaylist();
        playlist.setUser(executingUser); // Assuming the same user created the playlist
        // Act
        playlistService.delete(admin, playlist);
        // Assert
        verify(playlistRepository, times(1)).delete(playlist.getId());
    }

    @Test
    void delete_ShouldThrowAuthorizationExceptionForNonAdminUsers() {
        User executingUser = UserHelpers.createMockUser();
        User differentUser = UserHelpers.createDifferentuser();
        Playlist playlist = PlaylistDataHelpers.createPlaylist();
        playlist.setUser(executingUser);

        assertThrows(AuthorizationException.class, () -> playlistService.delete(differentUser, playlist));
    }

    @Test
    void allPlaylistsCount_ShouldReturnCountFromRepository() {
        // Arrange
        when(playlistRepository.allPlaylistsCount()).thenReturn(5L);
        // Act
        Long count = playlistService.allPlaylistsCount();
        // Assert
        assertEquals(5L, count);
    }

    @Test
    void getAllByCreator_ShouldReturnListOfPlaylistsFromRepository() {
        // Arrange
        int creatorId = 1;
        List<Playlist> expectedPlaylists = Arrays.asList(new Playlist(), new Playlist());
        when(playlistRepository.getAllByCreator(creatorId)).thenReturn(expectedPlaylists);
        // Act
        List<Playlist> actualPlaylists = playlistService.getAllByCreator(creatorId);
        // Assert
        assertEquals(expectedPlaylists, actualPlaylists);
    }
}
