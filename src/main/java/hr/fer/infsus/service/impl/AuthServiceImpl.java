package hr.fer.infsus.service.impl;

import hr.fer.infsus.dto.LoginDto;
import hr.fer.infsus.dto.RegisterDto;
import hr.fer.infsus.dto.UserDto;
import hr.fer.infsus.dto.response.LoginResponse;
import hr.fer.infsus.model.Role;
import hr.fer.infsus.model.User;
import hr.fer.infsus.repository.UserRepository;
import hr.fer.infsus.security.TokenProvider;
import hr.fer.infsus.service.AuthService;
import hr.fer.infsus.util.mapper.UserMapper;
import hr.fer.infsus.util.validator.AccountValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final AccountValidator accountValidator;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;
    private final UserMapper userMapper;

    public UserDto getCurrentUser() {
        final User user = getUserFromToken();

        return userMapper.userToUserDto(user);
    }

    public LoginResponse login(final LoginDto loginDto) {
        final Authentication auth = new UsernamePasswordAuthenticationToken(loginDto.username(), loginDto.password());
        authenticationManager.authenticate(auth);

        final User user = userRepository.findByUsername(loginDto.username());
        final String token = tokenProvider.generateToken(user.getUsername(), user.getRole());
        final UserDto userDto = userMapper.userToUserDto(user);
        return new LoginResponse(userDto, token);
    }

    public void register(final RegisterDto registerDto) {
        accountValidator.usernameTaken(registerDto.username());
        accountValidator.emailExists(registerDto.email());

        final User user = userMapper.registerDtoToUser(registerDto);
        user.setRole(Role.ROLE_USER);
        user.setPassword(passwordEncoder.encode(registerDto.password()));

        userRepository.save(user);
    }

    private User getUserFromToken() {
        final String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByUsername(username);
    }
}