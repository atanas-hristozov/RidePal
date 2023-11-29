package com.example.ridepal.repositories.contracts;

import com.example.ridepal.models.Genre;
import com.example.ridepal.models.Playlist;
import com.example.ridepal.models.PlaylistFilterOptions;
import com.example.ridepal.models.Track;
import com.example.ridepal.models.dtos.PlaylistDisplayDto;
import org.hibernate.Session;

import java.util.List;
import java.util.Set;

public interface PlaylistRepository extends BaseCrudRepository<Playlist> {

    void createPlaylist(Playlist playlist);
    List<Playlist> getAllByFilterOptions(PlaylistFilterOptions playlistFilterOptions);

}
