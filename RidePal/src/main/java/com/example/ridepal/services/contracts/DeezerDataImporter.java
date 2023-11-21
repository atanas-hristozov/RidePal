package com.example.ridepal.services.contracts;

public interface DeezerDataImporter {

    public void importGenreData(String responseBody);

    public void importArtistDataForGenre(String responseBody, int genreId);


}
