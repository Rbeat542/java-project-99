package hexlet.code.component;

import hexlet.code.model.TaskStatus;
import hexlet.code.model.Label;
import hexlet.code.repository.LabelRepository;
import org.springframework.beans.factory.annotation.Autowired;
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
public class UserInitializer implements ApplicationRunner {

    @Autowired
    private final UserRepository userRepository;

    @Autowired
    private final TaskStatusRepository taskStatusRepository;

    @Autowired
    private final CustomUserDetailsService userService;

    @Autowired
    private LabelRepository labelRepository;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        var email = "hexlet@example.com";
        var userData = new User();
        userData.setEmail(email);
        userData.setPasswordDigest("qwerty");
        userService.createUser(userData);

        generateDefaultTaskStatuses();
        generateDefaultLabels();
    }

    public void generateDefaultTaskStatuses() {
        var defaultSlugsList = List.of("draft", "to_review", "to_be_fixed", "to_publish", "published");

        for (var element : defaultSlugsList) {
            var taskStatus = new TaskStatus();
            taskStatus.setSlug(element);
            taskStatus.setName(element.toUpperCase().replace("_"," "));
            taskStatusRepository.save(taskStatus);
        }
    }

    public void generateDefaultLabels() {
        var defaultLabelsList = List.of("feature", "bug");

        for (var element : defaultLabelsList) {
            var label = new Label();
            label.setName(element.toUpperCase().replace("_"," "));
            labelRepository.save(label);
        }
    }
}