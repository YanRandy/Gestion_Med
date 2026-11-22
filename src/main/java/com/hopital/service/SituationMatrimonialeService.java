package com.hopital.service;

import com.hopital.entity.SituationMatrimoniale;
import com.hopital.repository.SituationMatrimonialeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SituationMatrimonialeService {

    private final SituationMatrimonialeRepository situationMatrimonialeRepository;

    public SituationMatrimonialeService(SituationMatrimonialeRepository situationMatrimonialeRepository) {
        this.situationMatrimonialeRepository = situationMatrimonialeRepository;
    }

    public List<SituationMatrimoniale> findAll() {
        return situationMatrimonialeRepository.findAll();
    }
}
