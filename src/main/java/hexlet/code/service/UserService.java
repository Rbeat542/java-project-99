package hexlet.code.service;

import hexlet.code.dto.UserDTO;
import hexlet.code.dto.UserUpdateDTO;
import hexlet.code.dto.UserCreateDTO;
import hexlet.code.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.crossstore.ChangeSetPersister;
//import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import hexlet.code.mapper.UserMapper;

import java.util.List;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserMapper userMapper;

    public List<UserDTO> findAll() {
        var listOfUsers = userRepository.findAll();
        var usersDTO = listOfUsers.stream()
                .map(userMapper::map)
                .toList();
        return usersDTO;
    }

    public UserDTO findByID(Long id) {
        var user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("findbyid method error: Id " + id + " not found"));
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
                .orElseThrow(() -> new RuntimeException("PUT method error: Id " + id + " not found"));
        userMapper.update(userDTO, user);
        userRepository.save(user);
        return userMapper.map(user);
    }

    public void deleteUser(Long id) {
        var user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Delete method error: Id " + id + " not found"));
        userRepository.deleteById(id);
    }

}
