package com.example.pupilmed.models.server.visit;

public record VetVisitRequest(Integer id, String date, String hour, String visitType, String phoneNumer, Integer price, String petName) {
}
