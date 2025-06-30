package hexlet.code.service;

import hexlet.code.model.User;
import hexlet.code.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public final class AccessChecker {

    private final UserRepository userRepository;

    public AccessChecker(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public boolean canDoWithUser(Long id, Authentication authentication) {
        String email = authentication.getName();
        Optional<User> userOpt = userRepository.findById(id);

        if (userOpt.isEmpty()) {
            System.out.println("User with ID " + id + " not found");
            return false;
        }

        String targetEmail = userOpt.get().getEmail();
        boolean result = email.equals(targetEmail);

        System.out.printf("🔍 Checking access: auth.email = %s, target.email = %s → %s%n",
                email, targetEmail, result);
        return result;
    }
}
