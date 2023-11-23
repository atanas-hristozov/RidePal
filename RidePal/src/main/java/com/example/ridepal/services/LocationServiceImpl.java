package com.example.ridepal.services;

import com.example.ridepal.services.contracts.LocationService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
@Service
public class LocationServiceImpl implements LocationService {


    @Override
    public List<String> giveBackPossibleLocations(String responseBody) {
        List<String> possibleLocations = new ArrayList<>();
        try {
            // Parse JSON response
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(responseBody);

            //List of possible locations
            ArrayNode resourceSets = (ArrayNode) jsonNode.get("resourceSets");
            ArrayNode locations = (ArrayNode) resourceSets.get(0).get("resources");
            locations.forEach(location -> possibleLocations.add(location.toString()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return possibleLocations;
    }
}
