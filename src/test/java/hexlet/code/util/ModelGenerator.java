package hexlet.code.util;

//import exercise.model.Article;
import hexlet.code.model.Label;
import hexlet.code.model.Task;
import hexlet.code.model.TaskStatus;
import hexlet.code.model.User;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import net.datafaker.Faker;
import org.instancio.Instancio;
import org.instancio.Model;
import org.instancio.Select;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static org.instancio.Select.field;

@Getter
@Component
public class ModelGenerator {
    //private Model<Article> articleModel;
    private Model<TaskStatus> taskStatusModel;
    private Model<User> userModel;
    private Model<Task> taskModel;
    private Model<Label> labelModel;

    @Autowired
    private Faker faker;

    @PostConstruct
    private void init() {
        userModel = Instancio.of(User.class)
                .ignore(field(User::getId))
                .supply(field(User::getFirstName), () -> faker.name().firstName())
                .supply(field(User::getLastName), () -> faker.name().lastName())
                .supply(field(User::getEmail), () -> faker.internet().emailAddress())
                .supply(field(User::getPasswordDigest), () -> faker.internet().password(3, 20))
                .toModel();

        taskStatusModel = Instancio.of(TaskStatus.class)
                .ignore(field(TaskStatus::getId))
                .supply(field(TaskStatus::getName), () -> faker.lorem().word())
                .supply(field(TaskStatus::getSlug), () -> faker.lorem().word())
                .toModel();

        taskModel = Instancio.of(Task.class)
                .ignore(field(Task::getId))
                .supply(field(Task::getIndex), () -> Long.parseLong(faker.number().digits(4)))
                .supply(field(Task::getName), () -> faker.lorem().word())
                .supply(field(Task::getDescription), () -> faker.lorem().sentence())
                .supply(Select.field(Task::getTaskStatus), () -> Instancio.of(taskStatusModel).create())
                .supply(field(Task::getLabels), () -> Instancio.of(labelModel).create()) // HERE !!
                .toModel();

        labelModel = Instancio.of(Label.class)
                .ignore(field(Label::getId))
                .supply(field(Label::getName), () -> faker.lorem().word())
                .toModel();
    }
}
