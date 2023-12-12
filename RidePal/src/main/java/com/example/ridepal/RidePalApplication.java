package com.example.ridepal;

import com.example.ridepal.services.DeezerDataImporterImpl;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class RidePalApplication {
    public static void main(String[] args) {
        SpringApplication.run(RidePalApplication.class, args);
    }
}
