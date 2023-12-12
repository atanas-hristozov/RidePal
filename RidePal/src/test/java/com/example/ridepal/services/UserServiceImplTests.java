package com.example.ridepal.services;

import com.example.ridepal.UserHelpers;
import com.example.ridepal.exceptions.EntityDuplicateException;
import com.example.ridepal.exceptions.EntityNotFoundException;
import com.example.ridepal.models.User;
import com.example.ridepal.models.UserFilterOptions;
import com.example.ridepal.repositories.contracts.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTests {

    @Mock
    UserRepository mockRepository;
    @InjectMocks
    UserServiceImpl mockService;

    @Test
    public void testGetAll() {
        User user = UserHelpers.createMockUser();
        User admin = UserHelpers.createAdmin();


        List<User> users = new ArrayList<>();
        users.add(user);
        users.add(admin);

        UserFilterOptions userFilterOptions = UserHelpers.createUserFilterOptions();
        when(mockRepository.getAllByFilterOptions(userFilterOptions)).thenReturn(users);

        List<User> result = mockService.getAllByFilterOptions(userFilterOptions);
        assertEquals(users, result);
    }

    @Test
    public void testGetById() {
        User user = UserHelpers.createMockUser();

        int userId = 2;
        when(mockRepository.getById(userId)).thenReturn(user);

        User result = mockService.getById(userId);

        assertEquals(user, result);
    }

    @Test
    public void testGetByName() {
        User user = UserHelpers.createMockUser();
        String username = user.getUsername();

        when(mockService.getByUsername(username)).thenReturn(user);
        User result = mockService.getByUsername(username);

        assertEquals(user, result);
    }

    @Test
    public void testShowUsersCount(){
        User user = UserHelpers.createMockUser();
        User user2 = UserHelpers.createAdmin();
        List<User> users = new ArrayList<>();
        users.add(user);
        users.add(user2);

        when(mockRepository.allUsersCount()).thenReturn((long) users.size());
        long result = mockService.allUsersCount();

        assertEquals(users.size(), result);
    }

    @Test
    public void testCreate(){
        User userToCreate = UserHelpers.createMockUser();
        when(mockRepository.getByField("username", userToCreate.getUsername()))
                .thenThrow(new EntityNotFoundException("User", "username", userToCreate.getUsername()));

        mockService.create(userToCreate);

        verify(mockRepository).create(userToCreate);
    }
    @Test
    public void testShouldThrowException_WhenCreateWithDuplicateUsername() {
        User user = UserHelpers.createMockUser();
        User user1 = UserHelpers.createMockUser();

        when(mockRepository.getByField("username", user.getUsername())).thenReturn(user1);

        assertThrows(EntityDuplicateException.class, () -> mockService.create(UserHelpers.createMockUser()));
    }


    @Test
    public void testUpdate() {
        User userToUpdate = UserHelpers.createMockUser();

        mockService.update(userToUpdate);

        verify(mockRepository).update(userToUpdate);
    }
    @Test
    public void testDelete() {
        User userToDelete = UserHelpers.createMockUser();

        mockService.delete(userToDelete);

        verify(mockRepository).delete(userToDelete.getId());
    }
}
