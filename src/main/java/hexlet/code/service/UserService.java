package hexlet.code.service;

import hexlet.code.dto.user.UserDTO;
import hexlet.code.dto.user.UserUpdateDTO;
import hexlet.code.dto.user.UserCreateDTO;
import hexlet.code.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import hexlet.code.mapper.UserMapper;
import java.util.List;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final UserMapper userMapper;

    public List<UserDTO> findAll() {
        var listOfUsers = userRepository.findAll();
        var usersDTO = listOfUsers.stream()
                .map(userMapper::map)
                .toList();
        return usersDTO;
    }

    public UserDTO findByID(Long id) {
        var user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User with id = " + id + " not found"));
        return userMapper.map(user);
    }

    public UserDTO createUser(UserCreateDTO userCreateDTO) {
        var user = userMapper.map(userCreateDTO);
        userRepository.save(user);
        var userDTO = userMapper.map(user);
        return userDTO;
    }

    public UserDTO updateUser(Long id, UserUpdateDTO userDTO) {
        var user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User with id = " + id + " not found"));
        userMapper.update(userDTO, user);
        userRepository.save(user);
        return userMapper.map(user);
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}
