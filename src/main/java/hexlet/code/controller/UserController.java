package hexlet.code.controller;

import hexlet.code.dto.UserCreateDTO;
import hexlet.code.dto.UserDTO;
import hexlet.code.model.User;
import hexlet.code.service.AccessChecker;
import hexlet.code.util.UserUtils;
import hexlet.code.dto.UserUpdateDTO;
import hexlet.code.repository.UserRepository;
import hexlet.code.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserUtils userUtils;

    @Autowired
    private AccessChecker accessChecker;

    @GetMapping("")
    public ResponseEntity<List<UserDTO>> index() {
        var users = userService.findAll();

        return ResponseEntity.ok()
                .header("X-Total-Count", String.valueOf(users.size()))
                .body(users);
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<UserDTO> show(@PathVariable Long id) {
        var user =  userService.findByID(id);

        return ResponseEntity.ok()
                .body(user);
    }

    @PostMapping(path = "")
    public ResponseEntity<UserDTO> create(@Valid @RequestBody UserCreateDTO userData) {
        var user =  userService.createUser(userData);

        return ResponseEntity.status(201)
                .body(user);
    }

    @PutMapping(path = "/{id}")
    @PreAuthorize("@userRepository.findById(#id).get().getEmail() == authentication.name")
    //@PreAuthorize("@accessChecker.canDoWithUser(#id, authentication)")  // с вынесением проверки в отдельный класс
    public ResponseEntity<UserDTO> patch(@PathVariable Long id, @RequestBody UserUpdateDTO dto) {
        var user = userService.updateUser(id, dto);

        return ResponseEntity.ok()
                .body(user);
    }

    @DeleteMapping(path = "/{id}")
    //@PreAuthorize("@accessChecker.canDeleteUser(#id, authentication)")  // с вынесением проверки в отдельный класс
    @PreAuthorize("@userRepository.findById(#id).get().getEmail() == authentication.name")
    public void delete(@PathVariable Long id) {    // here was a 403 status in test , but should 200
        userService.deleteUser(id);
    }



}

