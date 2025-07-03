package hexlet.code.util;

import hexlet.code.model.Label;
import hexlet.code.model.Task;
import hexlet.code.model.TaskStatus;
import hexlet.code.model.User;
import hexlet.code.repository.LabelRepository;
import hexlet.code.repository.TaskRepository;
import hexlet.code.repository.TaskStatusRepository;
import hexlet.code.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import net.datafaker.Faker;
import org.instancio.Instancio;
import org.instancio.Model;
import org.instancio.Select;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.Set;
import static org.instancio.Select.field;

@Component
public class ModelGenerator {
    private Model<TaskStatus> taskStatusModel;
    private Model<User> userModel;
    private Model<Task> taskModel;
    private Model<Label> labelModel;
    private Set<Label> labelSet;
    private Label label;
    private TaskStatus taskStatus;
    private User user;

    @Autowired
    private Faker faker;

    @Autowired
    private LabelRepository labelRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TaskStatusRepository taskStatusRepository;
    @Autowired
    private TaskRepository taskRepository;

    @PostConstruct
    public void init() {

        userModel = Instancio.of(User.class)
                .ignore(field(User::getId))
                .supply(field(User::getFirstName), () -> faker.name().firstName())
                .supply(field(User::getLastName), () -> faker.name().lastName())
                .supply(field(User::getEmail), () -> faker.internet().emailAddress())
                .supply(field(User::getPasswordDigest), () -> faker.internet().password(3, 20))
                .toModel();

        user = Instancio.of(userModel).create();
        user = userRepository.save(user);


        taskStatusModel = Instancio.of(TaskStatus.class)
                .ignore(field(TaskStatus::getId))
                .supply(field(TaskStatus::getName), () -> faker.lorem().word())
                .supply(field(TaskStatus::getSlug), () -> faker.lorem().word())
                .toModel();

        taskStatus = Instancio.of(taskStatusModel).create();
        taskStatus = taskStatusRepository.save(taskStatus);


        labelModel = Instancio.of(Label.class)
                .ignore(field(Label::getId))
                .supply(field(Label::getName), () -> faker.lorem().characters(3, 20))
                .toModel();

        label = Instancio.of(labelModel).create();
        label = labelRepository.save(label);
        labelSet = Set.of(label);

        taskModel = Instancio.of(Task.class)
                .ignore(field(Task::getId))
                .supply(field(Task::getIndex), () -> Integer.parseInt(faker.number().digits(4)))
                .supply(field(Task::getName), () -> faker.lorem().word())
                .supply(field(Task::getDescription), () -> faker.lorem().sentence())
                .supply(Select.field(Task::getTaskStatus), () -> taskStatus)
                .supply(field(Task::getLabels), () -> labelSet)
                .supply(field(Task::getAssignee), () -> user)
                .toModel();

    }

    public Model<TaskStatus> getTaskStatusModel() {
        return taskStatusModel;
    }

    public Model<User> getUserModel() {
        return userModel;
    }

    public Model<Task> getTaskModel() {
        return taskModel;
    }

    public Model<Label> getLabelModel() {
        return labelModel;
    }

    public Faker getFaker() {
        return faker;
    }
}
