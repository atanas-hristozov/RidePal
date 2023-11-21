package com.example.ridepal.repositories.contracts;

import com.example.ridepal.models.Track;

public interface TrackRepository {

    public void create(Track track);

    Track getById(int id);

}
