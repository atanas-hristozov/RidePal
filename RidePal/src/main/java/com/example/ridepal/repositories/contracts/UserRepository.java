package com.example.ridepal.repositories.contracts;

import com.example.ridepal.models.User;
import com.example.ridepal.models.UserFilterOptions;

import java.util.List;

public interface UserRepository extends BaseCrudRepository<User>{
    List<User>getAllByFilterOptions(UserFilterOptions userFilterOptions);
}
