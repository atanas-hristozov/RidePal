package com.example.ridepal;

import com.example.ridepal.services.DeezerDataImporterImpl;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class RidePalApplication {
    private final DeezerDataImporterImpl deezerDataImporter;

    @Autowired
    public RidePalApplication(DeezerDataImporterImpl deezerDataImporter) {
        this.deezerDataImporter = deezerDataImporter;
    }

    public static void main(String[] args) {
        SpringApplication.run(RidePalApplication.class, args);
    }

    @PostConstruct
    public void init() {
        //deezerDataImporter.importGenreData("");
        //deezerDataImporter.importArtistDataForGenre("165"); //R&B*/
        //deezerDataImporter.importArtistDataForGenre("152"); //rock
        //deezerDataImporter.importArtistDataForGenre("113");//Dance
        //deezerDataImporter.importArtistDataForGenre("95");//Kids
        //deezerDataImporter.importArtistDataForGenre("116");//Hiphop
        //deezerDataImporter.importArtistDataForGenre("98");//Classical
    }
}
