package com.example.ridepal.services;

import com.example.ridepal.PlaylistDataHelpers;
import com.example.ridepal.models.Genre;
import com.example.ridepal.repositories.AbstractCrudRepository;
import com.example.ridepal.repositories.contracts.BaseCrudRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class GenreServiceImplTests {
    @Mock
    AbstractCrudRepository<Genre> mockRepository;
    @InjectMocks
    GenreServiceImpl mockService;


    @Test
    void delete_ShouldInvokeRepositoryDelete() {
        // Arrange
        Genre genre = PlaylistDataHelpers.createGenre();

        // Act
        mockService.delete(genre.getGenreId());


        // Assert
        verify(mockRepository, times(1)).delete(genre.getGenreId());
    }

    @Test
    void getById_ShouldReturnGenreFromRepository() {
        // Arrange
        int genreId = 1;
        Genre expectedGenre = new Genre();
        when(mockRepository.getById(genreId)).thenReturn(expectedGenre);

        // Act
        Genre actualGenre = mockService.getById(genreId);

        // Assert
        assertEquals(expectedGenre, actualGenre);
    }

    @Test
    void getByName_ShouldReturnGenreFromRepository() {
        // Arrange
        String genreName = "Rock";
        Genre expectedGenre = new Genre();
        when(mockRepository.getByField("genreName", genreName)).thenReturn(expectedGenre);

        // Act
        Genre actualGenre = mockService.getByName(genreName);

        // Assert
        assertEquals(expectedGenre, actualGenre);
    }

    @Test
    void getAll_ShouldReturnListOfGenresFromRepository() {
        // Arrange
        List<Genre> expectedGenres = Arrays.asList(new Genre(), new Genre());
        when(mockRepository.getAll()).thenReturn(expectedGenres);

        // Act
        List<Genre> actualGenres = mockService.getAll();

        // Assert
        assertEquals(expectedGenres, actualGenres);
    }

}
