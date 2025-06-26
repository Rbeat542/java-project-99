package hexlet.code.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import hexlet.code.dto.UserUpdateDTO;
import hexlet.code.mapper.TaskMapper;
import hexlet.code.model.Task;
import hexlet.code.repository.TaskRepository;
import hexlet.code.util.ModelGenerator;
import net.datafaker.Faker;
import org.instancio.Instancio;
import org.instancio.Select;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.nio.charset.StandardCharsets;

import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class TaskControllerTests {

    @Autowired
    TaskMapper TaskMapper;

    @Autowired
    TaskRepository taskRepository;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    private WebApplicationContext wac;

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ModelGenerator modelGenerator;

    private Task testTask;

    private Faker faker;
    
    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac)
                .defaultResponseCharacterEncoding(StandardCharsets.UTF_8)
                .apply(springSecurity())
                .build();
        testTask = Instancio.of(modelGenerator.getTaskModel()).create();
    }

    @Test
    public void testIndex() throws Exception {
        taskRepository.save(testTask);
        var result = mockMvc.perform(get("/api/tasks").with(jwt()))
                .andExpect(status().isOk())
                .andReturn();

        var body = result.getResponse().getContentAsString();
        assertThatJson(body).isArray();
    }

    @Test
    public void testShowTask() throws Exception {
        taskRepository.save(testTask);
        var id = testTask.getId();
        var request = get("/api/tasks/" + id).with(jwt());

        var result = mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn();

        var task = taskRepository.findById(id).get();
        assertThat(task.getName()).isEqualTo(testTask.getName());
        assertThat(task.getDescription()).isEqualTo(testTask.getDescription());
    }

    @Test
    public void testCreateTask() throws Exception {
        var request = post("/api/tasks").with(jwt())
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(testTask));

        mockMvc.perform(request)
                .andExpect(status().isCreated());

        var task = taskRepository.findByName(testTask.getName()).get();
        assertThat(task).isNotNull();
        assertThat(task.getName()).isEqualTo(testTask.getName());
        assertThat(task.getDescription()).isEqualTo(testTask.getDescription());
        assertThat(task.getIndex()).isEqualTo(testTask.getIndex());
    }

    @Test
    public void testUpdateTask() throws Exception {
        taskRepository.save(testTask);
        var id = testTask.getId();

        var taskUpdateData = Instancio.of(modelGenerator.getTaskModel()).create();

        var request = put("/api/tasks/" + id)
                .with(jwt())
                //.with(jwt().jwt(jwt -> jwt.claim("name", testTask.getName()).subject(testTask.getName())))  // an error here to fix
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(taskUpdateData));

        mockMvc.perform(request)
                .andExpect(status().isOk());

        var task = taskRepository.findById(id).get();
        assertThat(task.getName()).isEqualTo(taskUpdateData.getName());
        assertThat(task.getDescription()).isEqualTo(taskUpdateData.getDescription());
        //add body check for all
    }

    @Test
    public void testDeleteTask() throws Exception {
        taskRepository.save(testTask);
        var id = testTask.getId();

        var request = delete("/api/tasks/" + id)
                .with(jwt());

        mockMvc.perform(request)
                .andExpect(status().is(204));

        assertThat(taskRepository.findById(id)).isEmpty();
    }

    @Test
    public void testDeleteUnauthorizedTask() throws Exception {
        taskRepository.save(testTask);
        var id = testTask.getId();

        var request = delete("/api/tasks/" + id);
        mockMvc.perform(request)
                .andExpect(status().is(401));

        assertThat(taskRepository.findById(id)).isNotEmpty();
    }


}