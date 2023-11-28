package com.example.ridepal.repositories;

import com.example.ridepal.models.Genre;
import com.example.ridepal.models.Playlist;
import com.example.ridepal.models.PlaylistFilterOptions;
import com.example.ridepal.models.Track;
import com.example.ridepal.repositories.contracts.PlaylistRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.*;

import static java.lang.String.format;

@Repository
public class PlaylistRepositoryImpl extends AbstractCrudRepository<Playlist> implements PlaylistRepository {
    @Autowired
    public PlaylistRepositoryImpl(SessionFactory sessionFactory, Class<Playlist> clazz) {
        super(sessionFactory, clazz);
    }

    @Override
    public void createPlaylist(Playlist playlist) {
        try(Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.persist(playlist);
            session.getTransaction().commit();
        }
    }

    @Override
    public List<Playlist> getAll() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery(format("from %s order by rank desc", clazz.getSimpleName()), clazz).list();
        }
    }

    @Override
    public List<Playlist> getAllByFilterOptions(PlaylistFilterOptions playlistFilterOptions) {
        try (Session session = sessionFactory.openSession()) {
            List<String> filters = new ArrayList<>();
            Map<String, Object> params = new HashMap<>();

            playlistFilterOptions.getTitle().ifPresent(value -> {
                filters.add("title like :title");
                params.put("playlistTime", String.format("%%%s%%", value));
            });

            playlistFilterOptions.getPlaylistTime().ifPresent(value -> {
                filters.add("totalDuration like :playlistTime");
                params.put("playlistTime", String.format("%%%s%%", value));
            });

            playlistFilterOptions.getGenreName().ifPresent(value -> {
                filters.add("genreName like :genreName");
                params.put("genreName", String.format("%%%s%%", value));
            });



            StringBuilder queryString = new StringBuilder("from Playlist");
            if (!filters.isEmpty()) {
                queryString
                        .append(" where ")
                        .append(String.join(" and ", filters));
            }
            Query<Playlist> query = session.createQuery(queryString.toString(), Playlist.class);
            query.setProperties(params);
            return query.list();
        }

    }
}
