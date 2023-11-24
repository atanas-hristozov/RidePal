package com.example.ridepal.controllers;

import com.example.ridepal.services.contracts.DeezerDataImporter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;

@RestController
@RequestMapping("/api/roadbeat/genres")
public class DeezerDataImporterRestController {

    private final DeezerDataImporter deezerDataImporter;
    private final WebClient webClient;

    @Autowired
    public DeezerDataImporterRestController(DeezerDataImporter deezerDataImporter,
                                            WebClient webClient) {
        this.deezerDataImporter = deezerDataImporter;
        this.webClient = webClient;
    }

    @GetMapping()
    public ResponseEntity<String> importGenres() {

        String responseBody = webClient.get()
                .uri("https://api.deezer.com/genre")
                .retrieve()
                .bodyToMono(String.class)
                .block();

        deezerDataImporter.importGenreData(responseBody);

        return ResponseEntity.ok(responseBody);
    }

    @GetMapping("/genre/{id}")
    public ResponseEntity<String> importAllDataForGenre(@PathVariable int id) {
        String responseBody = webClient.get()
                .uri("https://api.deezer.com/genre/" + id + "/artists")
                .retrieve()
                .bodyToMono(String.class)
                .block();

        deezerDataImporter.importArtistDataForGenre(responseBody, id);

        return ResponseEntity.ok(responseBody);
    }
//        165"); //R&B*/
//        152"); //rock
//        113");//Dance
//        95");//Kids
//        116");//Hiphop
//        98");//Classical
}
