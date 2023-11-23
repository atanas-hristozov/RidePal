package com.example.ridepal.services;

import com.example.ridepal.exceptions.EntityNotFoundException;
import com.example.ridepal.models.Album;
import com.example.ridepal.models.Artist;
import com.example.ridepal.models.Genre;
import com.example.ridepal.models.Track;
import com.example.ridepal.repositories.contracts.*;
import com.example.ridepal.services.contracts.DeezerDataImporter;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DeezerDataImporterImpl implements DeezerDataImporter {

    private final BaseCreateReadRepository<Genre> genreBaseCreateReadRepository;
    private final BaseCreateReadRepository<Artist> artistBaseCreateReadRepository;
    private final BaseCreateReadRepository<Album> albumBaseCreateReadRepository;
    private final BaseCreateReadRepository<Track> trackBaseCreateReadRepository;

    @Autowired
    public DeezerDataImporterImpl(BaseCreateReadRepository<Genre> genreBaseCreateReadRepository,
                                  BaseCreateReadRepository<Artist> artistBaseCreateReadRepository,
                                  BaseCreateReadRepository<Album> albumBaseCreateReadRepository,
                                  BaseCreateReadRepository<Track> trackBaseCreateReadRepository) {
        this.genreBaseCreateReadRepository = genreBaseCreateReadRepository;
        this.artistBaseCreateReadRepository = artistBaseCreateReadRepository;
        this.albumBaseCreateReadRepository = albumBaseCreateReadRepository;
        this.trackBaseCreateReadRepository = trackBaseCreateReadRepository;
    }

    @Override
    public void importGenreData(String responseBody) {

        /*String apiUrl = "https://api.deezer.com/genre";*/

        try {
            // Make a request to the Deezer API
            /*Request request = new Request.Builder()
                    .url(apiUrl)
                    .build();
            try (Response response = new OkHttpClient().newCall(request).execute()) {
                responseBody = response.body().string();
            }*/

            // Parse JSON response
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(responseBody);

            //List of genres
            ArrayNode dataArray = (ArrayNode) jsonNode.get("data");
            dataArray.forEach(this::createGenre);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void importArtistDataForGenre(String responseBody, int genreId) {
        String apiUrl = "https://api.deezer.com/genre/" + genreId + "/artists";

        try {
            // Make a request to the Deezer API
            Request request = new Request.Builder()
                    .url(apiUrl)
                    .build();
            try (Response response = new OkHttpClient().newCall(request).execute()) {
                responseBody = response.body().string();
            }

            // Parse JSON response
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(responseBody);

            //List of artists for the specific genre
            ArrayNode dataArray = (ArrayNode) jsonNode.get("data");
            dataArray.forEach(item -> createArtist(item, genreId));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void importTracksForArtist(Artist artist, int genreId) {
        String tracklistUrl = artist.getArtistTracklist();
        try {
            // Make a request to the Deezer API
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(tracklistUrl)
                    .build();
            String responseBody;
            try (Response response = client.newCall(request).execute()) {
                responseBody = response.body().string();
            }

            // Parse JSON response
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(responseBody);

            //List of tracks for a specific artist
            ArrayNode dataArray = (ArrayNode) jsonNode.get("data");
            dataArray.forEach(node -> createTrack(node, artist, genreId));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void createTrack(JsonNode item, Artist artist, int genreId) {
        int trackId = item.get("id").asInt();
        Album album;
        try {
            trackBaseCreateReadRepository.getById(trackId);

        } catch (EntityNotFoundException e) {
            String title = item.get("title").asText();
            int duration = item.get("duration").asInt();
            double rank = item.get("rank").asDouble();
            String previewUrl = item.get("preview").asText();
            JsonNode albumArray = item.get("album");
            album = createAlbum(albumArray, genreId);


            Track track = new Track();
            track.setTrackId(trackId);
            track.setTitle(title);
            track.setDuration(duration);
            track.setRank(rank);
            track.setPreviewUrl(previewUrl);
            track.setArtist(artist);
            track.setAlbum(album);
            track.setGenre(genreBaseCreateReadRepository.getById(genreId));

            trackBaseCreateReadRepository.create(track);
        }
    }

    private Album createAlbum(JsonNode item, int genreId) {
        int albumId = item.get("id").asInt();
        try {
            albumBaseCreateReadRepository.getById(albumId);
        } catch (EntityNotFoundException e) {
            String albumName = item.get("title").asText();
            String albumTracklist = item.get("tracklist").asText();

            Album album = new Album();
            album.setAlbumId(albumId);
            album.setAlbumName(albumName);
            album.setTracklistUrl(albumTracklist);
            album.setGenre(genreBaseCreateReadRepository.getById(genreId));

            albumBaseCreateReadRepository.create(album);
            return albumBaseCreateReadRepository.getById(albumId);
        }
        return null;
    }

    private void createArtist(JsonNode item, int genreId) {
        int artistId = item.get("id").asInt();
        try {
            artistBaseCreateReadRepository.getById(artistId);
        } catch (EntityNotFoundException e) {
            String artistName = item.get("name").asText();
            String artistPhoto = item.get("picture").asText();
            String artistTracklist = item.get("tracklist").asText();

            Artist artist = new Artist();
            artist.setArtistId(artistId);
            artist.setArtistPhoto(artistPhoto);
            artist.setArtistName(artistName);
            artist.setArtistTracklist(artistTracklist);

            artistBaseCreateReadRepository.create(artist);
            importTracksForArtist(artist, genreId);
        }
    }

    private void createGenre(JsonNode item) {
        int genreId = item.get("id").asInt();
        try {
            genreBaseCreateReadRepository.getById(genreId);
        } catch (EntityNotFoundException e) {
            String genreName = item.get("name").asText();

            // Create a JPA entity and save it
            Genre genreEntity = new Genre();
            genreEntity.setGenreId(genreId);
            genreEntity.setGenreName(genreName);

            genreBaseCreateReadRepository.create(genreEntity);
        }
    }

}

