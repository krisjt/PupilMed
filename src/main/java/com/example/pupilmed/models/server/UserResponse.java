package com.example.pupilmed.models.server;

import com.example.pupilmed.models.database.Role;

public record UserResponse(Integer id, Role role, String name, String surname, String phoneNumber, String clinicAddress, String clinicName, boolean active) {
}
