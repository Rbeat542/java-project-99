package hexlet.code.component;

import hexlet.code.model.TaskStatus;
import hexlet.code.model.Label;
import hexlet.code.repository.LabelRepository;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import hexlet.code.model.User;
import hexlet.code.repository.UserRepository;
import hexlet.code.repository.TaskStatusRepository;
import hexlet.code.service.CustomUserDetailsService;
import lombok.AllArgsConstructor;
import java.util.List;

@Component
@AllArgsConstructor
public final class UserInitializer implements ApplicationRunner {

    private final UserRepository userRepository;

    private final TaskStatusRepository taskStatusRepository;

    private final CustomUserDetailsService userService;

    private final LabelRepository labelRepository;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        if (!userRepository.existsByEmail("hexlet@example.com")) {
            var email = "hexlet@example.com";
            var userData = new User();
            userData.setEmail(email);
            userData.setPasswordDigest("qwerty");
            userService.createUser(userData);

            generateDefaultTaskStatuses();
            generateDefaultLabels();
        }
    }

    public void generateDefaultTaskStatuses() {
        var defaultSlugsList = List.of("draft", "to_review", "to_be_fixed", "to_publish", "published");

        for (var element : defaultSlugsList) {
            var taskStatus = new TaskStatus();
            taskStatus.setSlug(element);
            taskStatus.setName(element.toUpperCase().replace("_", " "));
            taskStatusRepository.save(taskStatus);
        }
    }

    public void generateDefaultLabels() {
        var defaultLabelsList = List.of("feature", "bug");

        for (var element : defaultLabelsList) {
            var label = new Label();
            label.setName(element.toUpperCase().replace("_", " "));
            labelRepository.save(label);
        }
    }
}
