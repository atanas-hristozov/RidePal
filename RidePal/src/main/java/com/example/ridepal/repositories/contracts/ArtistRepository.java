package com.example.ridepal.repositories.contracts;

import com.example.ridepal.models.Artist;

public interface ArtistRepository {

     void create(Artist artist);
     Artist getById(int id);

}
