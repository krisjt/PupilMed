package com.example.pupilmed.models.server;

import java.util.Date;

public record PetRequest(String ownerPhoneNumber, String petName, String petDob, String petSpecies, String petBreed, String additionalInfo) {
}
