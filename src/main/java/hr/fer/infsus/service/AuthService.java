package hr.fer.infsus.service;

import hr.fer.infsus.dto.LoginDto;
import hr.fer.infsus.dto.RegisterDto;
import hr.fer.infsus.dto.UserDto;
import hr.fer.infsus.dto.response.LoginResponse;

public interface AuthService {
    UserDto getCurrentUser();

    LoginResponse login(LoginDto loginDto);

    void register(RegisterDto registerDto);

}
