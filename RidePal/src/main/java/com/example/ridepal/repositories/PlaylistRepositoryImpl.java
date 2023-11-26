package com.example.ridepal.repositories;

import com.example.ridepal.models.Genre;
import com.example.ridepal.models.Playlist;
import com.example.ridepal.models.Track;
import com.example.ridepal.repositories.contracts.PlaylistRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public class PlaylistRepositoryImpl extends AbstractCrudRepository<Playlist> implements PlaylistRepository {
    @Autowired
    public PlaylistRepositoryImpl(SessionFactory sessionFactory, Class<Playlist> clazz) {
        super(sessionFactory, clazz);
    }

    @Override
    public void createPlaylist(Playlist playlist, Set<Track> tracks, Set<Genre> genres, Session session) {
            session.beginTransaction();
            session.persist(playlist);
            session.getTransaction().commit();
            session.close();
    }

    @Override
    public Session openSession() {
       return sessionFactory.openSession();
    }

/*    public void createPlaylist(Playlist playlist, String genreNames, int travelDuration) {
        List<String> genres = Arrays.stream(genreNames.split(", ")).toList();
        int durationPerGenre = travelDuration / genres.size();

        //finds the number of tracks per genre to be transferred based on the duration
        // given for the genre and the average length of a single track
        int tracksCountPerGenre = durationPerGenre / AVERAGE_TRACK_DURATION;
        int playlistDuration = 0;
        Set<Track> tracks;
        Set<Genre> genreSet = new HashSet<>();
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            //fill in tracks for each genre
            for (String genre : genres) {
                Query<Genre> query = session.createQuery("from Genre where genreName = :genreName", Genre.class);
                query.setParameter("genreName", genre);

                List<Genre> result = query.list();
                if (result.isEmpty()) {
                    throw new EntityNotFoundException("Genre", "name", genre);
                }
                Genre genreFound = result.get(0);
                genreSet.add(genreFound);

                //generate a number of tracks for the given genre
                Query<Track> query2 = session.createQuery("FROM Track WHERE genre = :genre ORDER BY rand()");
                query2.setParameter("genre", genreFound);
                query2.setMaxResults(tracksCountPerGenre);
                List<Track> result2 = query2.list();
                tracks = new HashSet<>(result2);
                if (tracks.isEmpty()) {
                    throw new EntityNotFoundException("Tracks", "genre", genre);
                }
                //find what duration is reached so far
                int sumDuration = tracks.stream().mapToInt(Track::getDuration).sum();
                //if the calculated duration is not enough add more
                if (sumDuration + 100 < durationPerGenre) {
                    tracksCountPerGenre = (durationPerGenre - sumDuration) / AVERAGE_TRACK_DURATION;
                    query2 = session.createQuery("FROM Track WHERE genre = :genre ORDER BY rand()");
                    query2.setParameter("genre", genreFound);
                    query2.setMaxResults(tracksCountPerGenre);
                    List<Track> resultSet = query2.list();
                    if (resultSet.isEmpty()) {
                        throw new EntityNotFoundException("Tracks", "genre", genre);
                    }
                    tracks.addAll(resultSet);
                }
                //if the calculated duration exceeds the limit remove some of the added tracks
                while (sumDuration > durationPerGenre + 180) {
                    tracks.remove(tracks.stream().findFirst().orElse(null));
                    sumDuration = tracks.stream().mapToInt(Track::getDuration).sum();
                }
                playlist.setTracks(tracks);
                //add to the total duration
                playlistDuration += sumDuration;
                genreSet.add(genreFound);
            }
            playlist.setRank(0.0);
            playlist.setPlaylistTime(playlistDuration);
            playlist.setGenres(genreSet);
            session.persist(playlist);
            session.getTransaction().commit();
        }
    }*/
}
