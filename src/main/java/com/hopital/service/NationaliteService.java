package com.hopital.service;

import com.hopital.entity.Nationalite;
import com.hopital.repository.NationaliteRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NationaliteService {

    private final NationaliteRepository nationaliteRepository;

    public NationaliteService(NationaliteRepository nationaliteRepository) {
        this.nationaliteRepository = nationaliteRepository;
    }

    public List<Nationalite> findAll() {
        return nationaliteRepository.findAll();
    }
}
