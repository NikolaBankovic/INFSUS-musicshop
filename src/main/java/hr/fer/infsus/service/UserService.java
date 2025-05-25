package hr.fer.infsus.service;

import hr.fer.infsus.dto.RegisterDto;
import hr.fer.infsus.dto.UpdateUserDto;
import hr.fer.infsus.dto.UserDto;

import java.util.List;

public interface UserService {
    List<UserDto> getAllUsers();

    UserDto getUserById(Long id);

    void createUser(RegisterDto userDto);

    void updateUser(Long id, UpdateUserDto userDto);

    void deleteUser(Long userId);
}
