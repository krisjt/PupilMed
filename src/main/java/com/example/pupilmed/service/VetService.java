package com.example.pupilmed.service;

import com.example.pupilmed.models.database.User;
import com.example.pupilmed.models.database.Vet;
import com.example.pupilmed.repositories.VetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class VetService {

    private final VetRepository vetRepository;

    @Autowired
    public VetService(VetRepository vetRepository) {
        this.vetRepository = vetRepository;
    }

    public List<Vet> getVets(){
        return vetRepository.findAll();
    }
    public Vet getVetByUser(User user){
        return vetRepository.getVetByUser(user);
    }
    public Vet getVetByUsername(String user){
        return vetRepository.getVetByUser_Username(user);
    }
    public void addVet(Vet vet) {
        Optional<Vet> vetByAddress = vetRepository.findVetByClinicAddress(vet.getClinicAddress());
        if(vetByAddress.isPresent()){
            throw new IllegalStateException("address taken");
        }
        vetRepository.save(vet);
    }

    public void deleteVet(int id) {
        boolean exists = vetRepository.existsById(id);
        if(exists){
            vetRepository.deleteById(id);
        }
        else{
            throw new IllegalStateException("Vet with id = " + id + " does not exist.");
        }
    }

    public void updateVet(int id, Vet vet) {
        if(id == vet.getId()){
            Optional<Vet> oldVet = vetRepository.findById(id);
            if(oldVet.isPresent() && oldVet.get().getUser()==vet.getUser()) {
                boolean exists = vetRepository.existsById(id);
                if (exists) {
                    vetRepository.save(vet);
                } else {
                    throw new IllegalStateException("Vet with id = " + id + " does not exist.");
                }
            }
            else  throw new IllegalStateException("Cannot change user id! Trying to change from " + oldVet.get().getUser() + " to " + vet.getUser() + ".");
        }
        else throw new IllegalStateException("Cannot change id of vet from " + id + " to " + vet.getId() + ".");
    }

}

//zanim dodamy weterynarza, to dodajemy użytkownika
// i czekamy na informacje, czy już istnieje, jak dodany zostanie, to zwraca nam jego id i monad stworzyć weterynarza
// jak się nie uda to zwracamy bld