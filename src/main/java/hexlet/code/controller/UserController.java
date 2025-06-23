package hexlet.code.controller;

import hexlet.code.dto.UserCreateDTO;
import hexlet.code.dto.UserDTO;
import hexlet.code.util.UserUtils;
import hexlet.code.dto.UserUpdateDTO;
import hexlet.code.repository.UserRepository;
import hexlet.code.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserUtils userUtils;

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
    //@PreAuthorize(userRepository.findByEmail().get().getEmail() == userUtils.getCurrentUser().getName())
    //@PreAuthorize("@userUtils.getCurrentUser() == userUtils.getTestUser()")
    //@ResponseStatus(HttpStatus.CREATED)
    //@PreAuthorize("@userUtils.getCurrentUser().getName() == (userUtils.getTestUser().getName() || userRepository.findById(#id).getEmail())")
    public ResponseEntity<UserDTO> create(@Valid @RequestBody UserCreateDTO userData) {
        var user =  userService.createUser(userData);

        return ResponseEntity.status(201)
                .body(user);
    }

    @PutMapping(path = "/{id}")
    //@PreAuthorize("(@userRepository.findById(#id).orElse(null)?.email == authentication.name) or (@userRepository.findById(#id).orElse(null)?.email == @userUtils.getTestUser().name)")
    //@PreAuthorize("@userRepository.findById(#id).get().getEmail().equalsTo(authentication.name) || @userRepository.findById(#id).get().getEmail().equalsTo(@userUtils.getTestUser().getName())")
    //@PreAuthorize("@userUtils.getCurrentUser().getName() == (userUtils.getTestUser().getName() || userRepository.findById(#id).getEmail())")
    @PreAuthorize("@userRepository.findById(#id).get().getEmail() == authentication.name")
    public ResponseEntity<UserDTO> patch(@PathVariable Long id, @RequestBody UserUpdateDTO dto) {
        var user = userService.updateUser(id, dto);

        return ResponseEntity.ok()
                .body(user);
    }

    @DeleteMapping(path = "/{id}")
    //@PreAuthorize("@userRepository.findById(#id).get().getEmail() == authentication.name || @userUtils.getCurrentUser().getName() == @userUtils.getTestUser().getEmail()")
    @PreAuthorize("@userRepository.findById(#id).get().getEmail() == authentication.name")
    //@PreAuthorize("@userUtils.getCurrentUser().getName() == (userUtils.getTestUser().getEmail() || userRepository.findById(#id).getEmail())")
    public void delete(@PathVariable Long id) {    // here was a 403 status in test , but should 200
        userService.deleteUser(id);
    }



}

