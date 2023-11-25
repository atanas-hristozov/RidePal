package com.example.ridepal.services;

import com.example.ridepal.models.Genre;
import com.example.ridepal.models.Playlist;
import com.example.ridepal.models.Track;
import com.example.ridepal.models.User;
import com.example.ridepal.repositories.contracts.BaseCrudRepository;
import com.example.ridepal.repositories.contracts.TrackRepository;
import com.example.ridepal.services.contracts.PlaylistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class PlaylistServiceImpl implements PlaylistService {
    private final static int AVERAGE_TRACK_DURATION = 210;//3:30 min
    private final BaseCrudRepository<Playlist> basePlaylistCrudRepository;
    private final TrackRepository trackRepository;


    @Autowired
    public PlaylistServiceImpl(BaseCrudRepository<Playlist> baseIdentificationUdRepository, TrackRepository trackRepository) {
        this.basePlaylistCrudRepository = baseIdentificationUdRepository;
        this.trackRepository = trackRepository;
    }

    @Override
    public Playlist getById(int id) {
        return basePlaylistCrudRepository.getById(id);
    }

    @Override
    public void create(User user, Playlist playlist, int travelDuration) {
        Set<Genre> genreList = playlist.getGenres();
        int durationPerGenre = travelDuration / genreList.size();

        //finds the number of tracks per genre to be transferred based on the duration
        // given for the genre and the average length of a single track
        int tracksCountPerGenre = durationPerGenre / AVERAGE_TRACK_DURATION;
        int playlistDuration = 0;
        Set<Track> tracks;
        //loop while the required duration is reached
        while (travelDuration + 150 > playlistDuration) {
            //fill in tracks for each genre
            for (Genre genre : genreList) {
                //generate a number of tracks for the given genre
                tracks = trackRepository.generateRandomTrackByGenre(genre, tracksCountPerGenre);
                //find what duration is reached so far
                int sumDuration = tracks.stream().mapToInt(Track::getDuration).sum();
                //if the calculated duration is not enough add more
                if (sumDuration + 100 < durationPerGenre) {
                    tracksCountPerGenre = (durationPerGenre - sumDuration) / AVERAGE_TRACK_DURATION;
                    tracks.addAll(trackRepository.generateRandomTrackByGenre(genre, tracksCountPerGenre));
                }
                //if the calculated duration exceeds the limit remove some of the added tracks
                while (sumDuration > durationPerGenre + 180) {
                    tracks.iterator().remove();
                    sumDuration = tracks.stream().mapToInt(Track::getDuration).sum();
                }
                //add the tracks for the concrete genre
                playlist.setTracks(tracks);
                //clear the variable for the new genre to collect
                tracks.clear();
                //add to the total duration
                playlistDuration += sumDuration;
            }
        }
        playlist.setRank(findAverageRank(playlist));
        playlist.setUser(user);
        playlist.setPlaylistTime(playlistDuration);
        basePlaylistCrudRepository.create(playlist);
    }

    @Override
    public void update(Playlist playlist) {
        basePlaylistCrudRepository.update(playlist);
    }

    @Override
    public void delete(int id) {
        basePlaylistCrudRepository.delete(id);
    }

    private double findAverageRank(Playlist playlist) {
     return playlist.getTracks().stream().mapToDouble(Track::getRank).sum() / playlist.getTracks().size();
    }
}
