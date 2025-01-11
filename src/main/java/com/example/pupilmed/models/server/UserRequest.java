package com.example.pupilmed.models.server;

public record UserRequest(String role, String name, String surname, String phoneNumber, String password, String clinicAddress, String clinicName) {
}
