package local.epul4a.fotosharing.service.impl;

import local.epul4a.fotosharing.dto.UserDto;
import local.epul4a.fotosharing.entity.Role;
import local.epul4a.fotosharing.entity.User;
import local.epul4a.fotosharing.repository.RoleRepository;
import local.epul4a.fotosharing.repository.UserRepository;
import local.epul4a.fotosharing.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Arrays.asList;
import static local.epul4a.fotosharing.enums.RoleType.USER;

@Service
public class UserServiceImpl implements UserService {
    private static String ROLE_PREFIX = "ROLE_";
    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;
    public UserServiceImpl(UserRepository userRepository,
                           RoleRepository roleRepository,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }
    @Override
    public void saveUser(UserDto userDto) {
        User user = new User();
        user.setName(userDto.getFirstName() + " " + userDto.getLastName());
        user.setEmail(userDto.getEmail());
        //encrypt the password once we integrate spring security
        //user.setPassword(userDto.getPassword());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        Role role = roleRepository.findByName(ROLE_PREFIX + USER.name());
        if(role == null){
            role = checkRoleExist();
        }
        user.setRoles(List.of(role));
        userRepository.save(user);
    }
    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
    @Override
    public List<UserDto> findAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream().map(this::convertEntityToDto)
                .collect(Collectors.toList());
    }
    @Override
    public List<String> findAllRoles() {
        return roleRepository.findAll().stream().map(Role::getName).toList();
    }
    private UserDto convertEntityToDto(User user){
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        String[] name = user.getName().split(" ");
        userDto.setFirstName(name[0]);
        userDto.setLastName(name[1]);
        userDto.setEmail(user.getEmail());
        userDto.setRoleName(user.role());
        return userDto;
    }
    private Role checkRoleExist() {
        Role role = new Role();
        role.setName(ROLE_PREFIX + USER.name());
        return roleRepository.save(role);
    }

    @Override
    public void modifyUser(Long id, String name) {
        User user = this.userRepository.findById(id).get();
        Role role = this.roleRepository.findByName(name);
        user.changeRole(role);
        this.userRepository.save(user);
    }
}