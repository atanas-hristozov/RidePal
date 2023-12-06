package com.example.ridepal.repositories.contracts;

import com.example.ridepal.models.Genre;
import com.example.ridepal.models.Track;
import org.hibernate.Session;

import java.util.List;
import java.util.Set;

public interface TrackRepository extends BaseCrudRepository<Track>{

   Set<Track> generateRandomTrackByGenre(Genre genre, int numberOfTracks);

   Long tracksNumberPerGenre(int genreId);
   Long allTracksNumber();
}
