package hexlet.code.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import hexlet.code.dto.task.TaskCreateDTO;
import hexlet.code.dto.task.TaskDTO;
import hexlet.code.dto.task.TaskUpdateDTO;
import hexlet.code.mapper.TaskMapper;
import hexlet.code.model.Label;
import hexlet.code.model.Task;
import hexlet.code.repository.LabelRepository;
import hexlet.code.repository.TaskRepository;
import hexlet.code.repository.TaskStatusRepository;
import hexlet.code.repository.UserRepository;
import hexlet.code.util.ModelGenerator;
import hexlet.code.util.TestKeyGenerator;
import org.instancio.Instancio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openapitools.jackson.nullable.JsonNullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class TaskControllerTests extends TestKeyGenerator {

    @Autowired
    LabelRepository labelRepository;

    @Autowired
    TaskRepository taskRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    TaskStatusRepository taskStatusRepository;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    private WebApplicationContext wac;

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ModelGenerator modelGenerator;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private Task testTask;

    @Autowired
    private TaskMapper taskMapper;

    @BeforeEach
    public void init() {
        taskRepository.deleteAll();
        labelRepository.deleteAll();
        taskStatusRepository.deleteAll();
        userRepository.deleteAll();
        modelGenerator.init();

        mockMvc = MockMvcBuilders.webAppContextSetup(wac)
                .defaultResponseCharacterEncoding(StandardCharsets.UTF_8)
                .apply(springSecurity())
                .build();
        testTask = Instancio.of(modelGenerator.getTaskModel()).create();
        taskRepository.save(testTask);
    }

    @Test
    public void testIndex() throws Exception {
        var result = mockMvc.perform(get("/api/tasks").with(jwt()))
                .andExpect(status().isOk())
                .andReturn();

        var body = result.getResponse().getContentAsString();
        List<TaskDTO> taskDTOS = om.readValue(body, new TypeReference<>() { });

        var expected = taskDTOS;
        var actual = taskRepository.findAll().stream().map(taskMapper::map).toList();
        assertThat(actual).containsExactlyInAnyOrderElementsOf(expected);
    }

    @Test
    public void testShowTask() throws Exception {
        var id = testTask.getId();
        var request = get("/api/tasks/" + id).with(jwt());

        var result = mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn();

        var body = result.getResponse().getContentAsString();
        assertThatJson(body).and(
                v -> v.node("title").isEqualTo(testTask.getName()),
                v -> v.node("content").isEqualTo(testTask.getDescription()));
    }

    @Test
    public void testCreateTask() throws Exception {
        var newTask = Instancio.of(modelGenerator.getTaskModel()).create();
        var newStatus = newTask.getTaskStatus();
        var newName = newTask.getName();

        var newData = new TaskCreateDTO();
        newData.setStatus(newStatus.getSlug());
        newData.setTitle(newName);

        var request = post("/api/tasks").with(jwt())
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(newData));

        mockMvc.perform(request)
                .andExpect(status().isCreated());

        var task = taskRepository.findFirstByNameOrderByCreatedAtDesc(newData.getTitle()).get();
        assertThat(task.getName()).isEqualTo(newName);
        assertThat(task.getTaskStatus().getId()).isEqualTo(newStatus.getId());
    }

    @Test
    public void testUpdateTask() throws Exception {
        var id = testTask.getId();
        var taskUpdateData = Instancio.of(modelGenerator.getTaskModel()).create();

        var updateData = new TaskUpdateDTO();
        updateData.setTitle(JsonNullable.of(taskUpdateData.getName()));
        updateData.setContent(JsonNullable.of(taskUpdateData.getDescription()));

        var request = put("/api/tasks/" + id)
                .with(jwt())
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(updateData));

        mockMvc.perform(request)
                .andExpect(status().isOk());

        var task = taskRepository.findById(id).get();
        assertThat(task.getName()).isEqualTo(taskUpdateData.getName());
        assertThat(task.getDescription()).isEqualTo(taskUpdateData.getDescription());
    }

    @Test
    public void testDeleteTask() throws Exception {
        var id = testTask.getId();
        var request = delete("/api/tasks/" + id)
                .with(jwt());

        mockMvc.perform(request)
                .andExpect(status().is(204));

        assertThat(taskRepository.findById(id)).isEmpty();
    }

    @Test
    public void testDeleteUnauthorizedTask() throws Exception {
        var id = testTask.getId();
        var request = delete("/api/tasks/" + id);

        mockMvc.perform(request)
                .andExpect(status().is(401));

        assertThat(taskRepository.findById(id)).isNotEmpty();
    }

    @Test
    public void testGetTasksWithParams() throws Exception {
        var status = testTask.getTaskStatus().getSlug();
        var assigneeId = testTask.getAssignee().getId();
        var title = testTask.getName();
        var labelId = testTask.getLabels().stream()
                .map(Label::getId)
                .findFirst()
                .orElse(null);

        var query = new StringBuilder("?");
        query.append("titleCont=").append(title);
        query.append("&status=").append(status);
        query.append("&assigneeId=").append(assigneeId);

        if (labelId != null) {
            query.append("&labelId=").append(labelId);
        }

        var request = get("/api/tasks" + query.toString()).with(jwt());

        var result  = mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn();

        var body = result.getResponse().getContentAsString();
        var taskDTOS = om.readValue(body, new TypeReference<List<TaskDTO>>() { });

        assertThat(testTask.getName()).isEqualTo(taskDTOS.get(0).getTitle());
        assertThat(testTask.getDescription()).isEqualTo(taskDTOS.get(0).getContent());
    }
}
