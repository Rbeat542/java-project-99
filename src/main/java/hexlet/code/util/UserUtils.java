package hexlet.code.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import hexlet.code.model.User;
import hexlet.code.repository.UserRepository;

@Component
public final class UserUtils {
    @Autowired
    private UserRepository userRepository;

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
