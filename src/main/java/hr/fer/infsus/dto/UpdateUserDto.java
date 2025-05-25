package hr.fer.infsus.dto;

import hr.fer.infsus.model.Role;

public record UpdateUserDto(
        String username,
        String email,
        String password,
        String phoneNumber,
        String address,
        Role role
) {
}