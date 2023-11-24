package com.example.ridepal.controllers;

import com.example.ridepal.models.dtos.LocationDto;
import com.example.ridepal.services.contracts.LocationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@RestController
@RequestMapping("/api/roadbeat/route")
public class BingMapsImportRestController {
    private final WebClient webClient;
    private final LocationService locationService;
        @Autowired
        public BingMapsImportRestController(WebClient webClient, LocationService locationService) {
            this.webClient = webClient;
            this.locationService=locationService;
        }

    @GetMapping
    public List<String> importPossibleLocations(@RequestHeader HttpHeaders headers,
                                                @Valid @RequestBody LocationDto locationDto) {
        String url = String.format("http://dev.virtualearth.net/REST/v1/Locations/%s=1/%s/%s/%s?key=%s",
                locationDto.getCountry(),
                locationDto.getAdministrativeDistrict(),
                locationDto.getLocality(),
                locationDto.getAddress(),
                headers.get("API-KEY").get(0)
                );

        String responseBody = webClient.get()
                .uri(url)
                .retrieve()
                .bodyToMono(String.class)
                .block();

       return locationService.giveBackPossibleLocations(responseBody);
    }

}
