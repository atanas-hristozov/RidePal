package com.example.ridepal;

import com.example.ridepal.models.*;

import java.util.HashSet;
import java.util.Set;

public class PlaylistDataHelpers {

    public static Playlist createPlaylist(){
        User user = UserHelpers.createMockUser();
        Track track = createTrack();
        Set<Track> tracks = new HashSet<>();
        tracks.add(track);
        Playlist playlist = new Playlist();
        playlist.setId(1);
        playlist.setTitle("testTitle");
        playlist.setRank(10.5);
        playlist.setPlaylistTime(3600);
        playlist.setUser(user);
        playlist.setTracks(tracks);
        return playlist;
    }
    public static Genre createGenre(){
        Genre genre = new Genre();
        genre.setGenreId(1);
        genre.setGenreName("Rock");
        return genre;
    }
    public static Artist createArtist(){
        Artist artist = new Artist();
        artist.setArtistId(1);
        artist.setArtistName("TestArtistName");
        artist.setArtistTracklist("https://api.deezer.com/artist/11/top?limit=50");
        artist.setArtistPhoto("https://api.deezer.com/artist/1/image");
        return artist;
    }
    public static Album createAlbum(){
        Album album = new Album();
        album.setAlbumId(1);
        album.setAlbumName("TestAlbumName");
        album.setTracklistUrl("https://api.deezer.com/album/42872/tracks");
        album.setGenre(createGenre());
        return album;
    }
    public static Track createTrack(){
        Track track = new Track();
        track.setTrackId(1);
        track.setTitle("TestTrackTitle");
        track.setDuration(360);
        track.setRank(10.5);
        track.setPreviewUrl("https://cdns-preview-4.dzcdn.net/stream/c-4eb38d794d8473e0e1065280e68eadde-2.mp3");
        track.setArtist(createArtist());
        track.setAlbum(createAlbum());
        track.setGenre(createGenre());
        return track;
    }
}
