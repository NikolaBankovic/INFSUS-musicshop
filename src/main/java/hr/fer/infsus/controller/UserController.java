package hr.fer.infsus.controller;

import hr.fer.infsus.dto.RegisterDto;
import hr.fer.infsus.dto.UpdateUserDto;
import hr.fer.infsus.dto.UserDto;
import hr.fer.infsus.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/all")
    public List<UserDto> getUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{userId}")
    public UserDto getUserById(@PathVariable final Long userId) {
        return userService.getUserById(userId);
    }

    @PostMapping
    public void createUser(@RequestBody final RegisterDto registerDto) {
        userService.createUser(registerDto);
    }

    @PutMapping("/{userId}")
    public void updateUser(@PathVariable final Long userId, @RequestBody final UpdateUserDto userUpdateDto) {
        userService.updateUser(userId, userUpdateDto);
    }

    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable final Long userId) {
        userService.deleteUser(userId);
    }
}