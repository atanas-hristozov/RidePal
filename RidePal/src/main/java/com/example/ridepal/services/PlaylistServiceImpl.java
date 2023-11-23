package com.example.ridepal.services;

import com.example.ridepal.models.Playlist;
import com.example.ridepal.repositories.contracts.BaseUpdateDeleteRepository;
import com.example.ridepal.services.contracts.PlaylistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PlaylistServiceImpl implements PlaylistService {

    BaseUpdateDeleteRepository<Playlist> baseIdentificationUdRepository;
    @Autowired
    public PlaylistServiceImpl(BaseUpdateDeleteRepository<Playlist> baseIdentificationUdRepository) {
        this.baseIdentificationUdRepository = baseIdentificationUdRepository;
    }

    @Override
    public Playlist getById(int id) {
        return baseIdentificationUdRepository.getById(id);
    }

    @Override
    public void create(Playlist playlist) {
        baseIdentificationUdRepository.create(playlist);
    }

    @Override
    public void update(Playlist playlist) {
        baseIdentificationUdRepository.update(playlist);
    }

    @Override
    public void delete(int id) {
        baseIdentificationUdRepository.delete(id);
    }
}
