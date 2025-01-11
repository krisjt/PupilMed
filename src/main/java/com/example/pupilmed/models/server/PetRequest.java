package com.example.pupilmed.models.server;

public record PetRequest(String ownerPhoneNumber, String petName, String petDob, String petSpecies, String petBreed, String additionalInfo) {
}
