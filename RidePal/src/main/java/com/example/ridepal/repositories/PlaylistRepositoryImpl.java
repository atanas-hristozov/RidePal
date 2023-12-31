package com.example.ridepal.repositories;

import com.example.ridepal.models.Genre;
import com.example.ridepal.models.Playlist;
import com.example.ridepal.models.PlaylistFilterOptions;
import com.example.ridepal.models.Track;
import com.example.ridepal.models.dtos.ArtistDisplayDto;
import com.example.ridepal.models.dtos.GenreDisplayDto;
import com.example.ridepal.models.dtos.PlaylistDisplayDto;
import com.example.ridepal.models.dtos.TrackDisplayDto;
import com.example.ridepal.repositories.contracts.PlaylistRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.jetbrains.annotations.NotNull;
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
        try (Session session = sessionFactory.openSession()) {
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
                filters.add("p.title like :title");
                params.put("title", String.format("%%%s%%", value));
            });

            playlistFilterOptions.getPlaylistTimeFrom().ifPresent(value -> {
                filters.add("p.playlistTime >= :playlistTimeFrom");
                params.put("playlistTimeFrom", value * 60);
            });

            playlistFilterOptions.getPlaylistTimeTo().ifPresent(value -> {
                filters.add("p.playlistTime <= :playlistTimeTo");
                params.put("playlistTimeTo", value * 60);
            });

            playlistFilterOptions.getGenreName().ifPresent(value -> {
                filters.add("g.genreName like :genreName");
                params.put("genreName", String.format("%%%s%%", value));
            });


            StringBuilder queryString = new StringBuilder("select distinct p from Playlist p" +
                    " JOIN p.genres g");
            if (!filters.isEmpty()) {
                queryString
                        .append(" where ")
                        .append(String.join(" and ", filters));
            }
            queryString.append(" order by rank desc");
            Query<Playlist> query = session.createQuery(queryString.toString(), Playlist.class);
            query.setProperties(params);
            return query.list();
        }
    }

    @Override
    public List<Playlist> getAllByCreator(int creatorId) {
        try (Session session = sessionFactory.openSession()) {
            String hql = "FROM Playlist P WHERE P.user.id = :creatorId";
            Query<Playlist> query = session.createQuery(hql, Playlist.class);
            query.setParameter("creatorId", creatorId);
            return query.getResultList();
        }
    }

    @Override
    public Long allPlaylistsCount() {
        try (Session session = sessionFactory.openSession()) {
            Query<Long> query = session.createQuery("SELECT COUNT(id) from Playlist", Long.class);
            return query.uniqueResult() != null ? query.uniqueResult() : 0;
        }
    }
}
