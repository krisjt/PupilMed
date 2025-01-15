package com.example.pupilmed.models.server;

public record PasswordChangeRequest(String newPassword, String currentPassword) {
}
