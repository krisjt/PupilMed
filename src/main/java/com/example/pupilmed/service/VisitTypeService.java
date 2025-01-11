package com.example.pupilmed.service;

import com.example.pupilmed.models.database.visitType.VisitType;
import com.example.pupilmed.repositories.VisitTypeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VisitTypeService {

    VisitTypeRepository visitTypeRepository;

    public VisitTypeService(VisitTypeRepository visitTypeRepository) {
        this.visitTypeRepository = visitTypeRepository;
    }

    public List<VisitType> getAll(){
        return visitTypeRepository.findAll();
    }

}
