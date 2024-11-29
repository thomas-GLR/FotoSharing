package local.epul4a.fotosharing.service;

import local.epul4a.fotosharing.dto.UserDto;
import local.epul4a.fotosharing.entity.User;

import java.util.List;
public interface UserService {
    void saveUser(UserDto userDto);
    User findByEmail(String email);
    List<UserDto> findAllUsers();
}
