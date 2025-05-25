package hr.fer.infsus.dto;

import hr.fer.infsus.model.Role;

public record UserDto(
        Long id,
        String firstName,
        String lastName,
        String username,
        String email,
        String phoneNumber,
        Role role
) {
}