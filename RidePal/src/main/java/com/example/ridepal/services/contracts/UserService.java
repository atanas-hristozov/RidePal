package com.example.ridepal.services.contracts;

import com.example.ridepal.models.User;
import com.example.ridepal.models.UserFilterOptions;
import com.example.ridepal.models.dtos.UserDisplayDto;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface UserService {
    public void create (User user);
    void delete (User user);
    void update (User userToUpdate);
    User getById (int id);
    User getByUsername(String username);
    List<User> getAllByFilterOptions(UserFilterOptions userFilterOptions);
    void uploadPhoto(User user, MultipartFile multipartFile) throws IOException;
}
