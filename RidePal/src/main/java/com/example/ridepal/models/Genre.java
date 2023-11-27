package com.example.ridepal.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "genres")
public class Genre {

    @Id
    @Column(name = "id")
    private int genreId;

    @Column(name = "genre_name")
    private String genreName;
   @ManyToMany(mappedBy = "genres", fetch = FetchType.EAGER, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    private Set<Playlist> playlists;

    public Genre() {
    }


    public int getGenreId() {
        return genreId;
    }

    public void setGenreId(int genreId) {
        this.genreId = genreId;
    }

    public String getGenreName() {
        return genreName;
    }

    public void setGenreName(String genreName) {
        this.genreName = genreName;
    }

    public Set<Playlist> getPlaylists() {
        return playlists;
    }

    public void addPlaylist(Playlist playlist) {
        if(this.playlists==null)
            this.playlists=new HashSet<>();

        playlists.add(playlist);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Genre genre = (Genre) o;
        return genreId == genre.genreId && genreName.equals(genre.getGenreName());
    }
  /* @Override
    public int hashCode() {
        int result = genreId;
        result = 31 * result + genreName.hashCode();
        return result;
    }*/
}
