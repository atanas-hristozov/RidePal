package com.example.ridepal.services.contracts;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public interface LocationService {



   List<String> giveBackPossibleLocations(String responseBody);
}
