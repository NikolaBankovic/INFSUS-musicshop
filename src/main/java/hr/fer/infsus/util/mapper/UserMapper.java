package hr.fer.infsus.util.mapper;

import hr.fer.infsus.dto.RegisterDto;
import hr.fer.infsus.dto.UserDto;
import hr.fer.infsus.model.User;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDto userToUserDto(User user);
    User userDtoToUser(UserDto userDto);
    List<UserDto> userListToUserDtoList(List<User> userList);
    List<User> userDtoListToUserList(List<UserDto> userDtoList);
    RegisterDto userToRegisterDto(User user);
    User registerDtoToUser(RegisterDto registerDto);
}