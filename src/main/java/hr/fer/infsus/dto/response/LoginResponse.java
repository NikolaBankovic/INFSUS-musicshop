package hr.fer.infsus.dto.response;

import hr.fer.infsus.dto.UserDto;

public record LoginResponse(
        UserDto user,
        String token
) {
}