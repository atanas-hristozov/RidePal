package com.example.ridepal.services;

import com.example.ridepal.exceptions.AuthorizationException;
import com.example.ridepal.models.*;
import com.example.ridepal.models.dtos.PlaylistDisplayDto;
import com.example.ridepal.repositories.PlaylistRepositoryImpl;
import com.example.ridepal.repositories.contracts.BaseCrudRepository;
import com.example.ridepal.repositories.contracts.PlaylistRepository;
import com.example.ridepal.repositories.contracts.TrackRepository;
import com.example.ridepal.services.contracts.PlaylistService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class PlaylistServiceImpl implements PlaylistService {
    public static final String GENRES_MATCH_ERROR = "Those Genres doesn't match the genres in the Api.";
    public static final String ERROR_MESSAGE = "You are not authorized!";

    private final static int AVERAGE_TRACK_DURATION = 210;//3:30 min

    //  private final BaseCrudRepository<Playlist> basePlaylistCrudRepository;
    private final TrackRepository trackRepository;
    private final BaseCrudRepository<Genre> genreBaseCrudRepository;

    private final PlaylistRepository playlistRepository;


    @Autowired
    public PlaylistServiceImpl(TrackRepository trackRepository,
                               BaseCrudRepository<Genre> genreBaseCrudRepository,
                               PlaylistRepository playlistRepository) {
        //    this.basePlaylistCrudRepository = basePlaylistCrudRepository;
        this.trackRepository = trackRepository;
        this.genreBaseCrudRepository = genreBaseCrudRepository;
        this.playlistRepository = playlistRepository;
    }

    @Override
    public Playlist getById(int id) {
        return playlistRepository.getById(id);
    }

    @Override
    public List<Playlist> getAll(PlaylistFilterOptions playlistFilterOptions) {
        return playlistRepository.getAllByFilterOptions(playlistFilterOptions);
    }

    @Override
    public void create(User user, Playlist playlist, int travelDuration, String genreNames) {
        List<String> namesGenre = Arrays.stream(genreNames.split(", ")).toList();

        int durationPerGenre = travelDuration / namesGenre.size();

        //finds the number of tracks per genre to be transferred based on the duration
        // given for the genre and the average length of a single track
        int tracksCountPerGenre = durationPerGenre / AVERAGE_TRACK_DURATION;
        int playlistDuration = 0;
        //open session for all entities involved
        // Session session = playlistRepository.openSession();

        //fill in tracks for each genre
        for (String name : namesGenre) {
            //generate a number of tracks for the given genre
            Genre genreEntity = genreBaseCrudRepository.getByField("genreName", name);
            playlist.addGenre(genreEntity);
            Set<Track> tracks = trackRepository.generateRandomTrackByGenre(genreEntity, tracksCountPerGenre);
            //find what duration is reached so far
            int sumDuration = tracks.stream().mapToInt(Track::getDuration).sum();
            //if the calculated duration is not enough add more
            if (sumDuration + 210 < durationPerGenre) {
                tracksCountPerGenre = (durationPerGenre - sumDuration) / AVERAGE_TRACK_DURATION;
                tracks.addAll(trackRepository.generateRandomTrackByGenre(genreEntity, tracksCountPerGenre));
            }
            //if the calculated duration exceeds the limit remove some of the added tracks
            while (sumDuration > durationPerGenre + 180) {
                tracks.remove(tracks.stream().findFirst().orElse(null));
                sumDuration = tracks.stream().mapToInt(Track::getDuration).sum();
            }
            playlist.setTracks(tracks);
            //add to the total duration
            playlistDuration += sumDuration;
        }
        playlist.setRank(findAverageRank(playlist));
        playlist.setPlaylistTime(playlistDuration);
        playlist.setUser(user);
        user.addPlaylist(playlist);
        playlistRepository.createPlaylist(playlist);
    }

    @Override
    public void update(User executingUser, Playlist playlist) {
        checkAccessPermissions(executingUser, playlist);
        playlistRepository.update(playlist);
    }

    @Override
    public void delete(User executingUser, Playlist playlist) {
        checkAccessPermissions(executingUser, playlist);
        playlistRepository.delete(playlist.getId());
    }

    private double findAverageRank(Playlist playlist) {
        return BigDecimal.valueOf(playlist.getTracks().stream()
                .mapToDouble(Track::getRank)
                .average().getAsDouble()).setScale(2, RoundingMode.UP).doubleValue();
    }

    private static void checkAccessPermissions(User executingUser, Playlist playlist) {
        if (!executingUser.isAdmin() && executingUser.getId() != playlist.getUser().getId() && executingUser.getId() != 1) {
            // User with Id 1 have admin rights by default
            throw new AuthorizationException(ERROR_MESSAGE);
        }
    }

    private Set<Genre> validateGenres(Set<Genre> jsonGenres) {
        Set<Genre> dbGenres = new HashSet<>(genreBaseCrudRepository.getAll());
        if (!jsonGenres.equals(dbGenres)) {
            throw new EntityNotFoundException(GENRES_MATCH_ERROR);
        }
        return jsonGenres;
    }
}