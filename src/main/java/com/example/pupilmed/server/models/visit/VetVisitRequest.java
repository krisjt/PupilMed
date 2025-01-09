package com.example.pupilmed.server.models.visit;

public record VetVisitRequest(Integer id, String date, String hour, String visitType, String phoneNumer, Integer price, String petName) {
}
