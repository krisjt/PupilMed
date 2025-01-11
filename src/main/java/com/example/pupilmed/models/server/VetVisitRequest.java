package com.example.pupilmed.models.server;

public record VetVisitRequest(Integer id, String date, String hour, String visitType, String ownerPhoneNumber, String vetPhoneNumber, Integer price, String petName) {
}
