package hr.fer.infsus.dto;

import hr.fer.infsus.model.Role;

public record RegisterDto(
        String firstName,
        String lastName,
        String username,
        String email,
        String password,
        String phoneNumber,
        String address,
        Role role
) {
}
