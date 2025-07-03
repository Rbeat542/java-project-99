package hexlet.code.util;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import hexlet.code.model.User;
import hexlet.code.repository.UserRepository;

@Component
@AllArgsConstructor
public class UserUtils {

    private final UserRepository userRepository;

/*    public Optional<User> getCurrentUser() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            return Optional.empty();
        }

        var email = authentication.getName();
        return userRepository.findByEmail(email);
    }*/

    public User getTestUser() {
        return  userRepository.findByEmail("hexlet@example.com")
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}
