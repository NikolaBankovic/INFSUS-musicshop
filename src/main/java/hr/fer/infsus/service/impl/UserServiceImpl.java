package hr.fer.infsus.service.impl;

import hr.fer.infsus.dto.RegisterDto;
import hr.fer.infsus.dto.UpdateUserDto;
import hr.fer.infsus.dto.UserDto;
import hr.fer.infsus.model.Role;
import hr.fer.infsus.model.User;
import hr.fer.infsus.repository.UserRepository;
import hr.fer.infsus.service.UserService;
import hr.fer.infsus.util.mapper.UserMapper;
import hr.fer.infsus.util.validator.AccountValidator;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final AccountValidator accountValidator;
    private final PasswordEncoder passwordEncoder;

    public List<UserDto> getAllUsers() {
        final List<User> userList = userRepository.findAll();

        return userMapper.userListToUserDtoList(userList);
    }

    public UserDto getUserById(final Long id) {
        final User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format("User with ID(%d) doesn't exist!", id)));
        return userMapper.userToUserDto(user);
    }


    public void createUser(final RegisterDto registerDto) {
        accountValidator.usernameTaken(registerDto.username());
        accountValidator.emailExists(registerDto.email());

        final User user = userMapper.registerDtoToUser(registerDto);
        user.setPassword(passwordEncoder.encode(registerDto.password()));
        if(registerDto.role() == null) {
            user.setRole(Role.ROLE_USER);
        }

        userRepository.save(user);
    }

    public void updateUser(final Long id, final UpdateUserDto updatedUser) {
        final User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format("User with ID(%d) doesn't exist!", id)));

        if (!user.getUsername().equals(updatedUser.username())) {
            accountValidator.usernameTaken(updatedUser.username());
        }
        if (!user.getEmail().equals(updatedUser.email())) {
            accountValidator.emailExists(updatedUser.email());
        }

        user.setUsername(updatedUser.username());
        user.setEmail(updatedUser.email());
        user.setPhoneNumber(updatedUser.phoneNumber());
        user.setRole(updatedUser.role());

        userRepository.save(user);
    }

    public void deleteUser(final Long userId) {
        final User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(String.format("User with ID(%d) doesn't exist!", userId)));
        userRepository.delete(user);
    }
}
