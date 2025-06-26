package hexlet.code.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import hexlet.code.mapper.TaskStatusMapper;
import hexlet.code.model.TaskStatus;
import hexlet.code.repository.TaskStatusRepository;
import hexlet.code.util.ModelGenerator;
import org.instancio.Instancio;
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
class TaskStatusControllerTests {

    @Autowired
    TaskStatusMapper TaskStatusMapper;

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

    private TaskStatus testTaskStatus;
    
    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac)
                .defaultResponseCharacterEncoding(StandardCharsets.UTF_8)
                .apply(springSecurity())
                .build();
        testTaskStatus = Instancio.of(modelGenerator.getTaskStatusModel()).create();
    }

    @Test
    public void testIndex() throws Exception {
        taskStatusRepository.save(testTaskStatus);
        var result = mockMvc.perform(get("/api/task_statuses").with(jwt()))
                .andExpect(status().isOk())
                .andReturn();

        var body = result.getResponse().getContentAsString();
        assertThatJson(body).isArray();
    }

    @Test
    public void testShowStatus() throws Exception {
        taskStatusRepository.save(testTaskStatus);
        var id = testTaskStatus.getId();
        var request = get("/api/task_statuses/" + id).with(jwt());

        var result = mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn();

        var task = taskStatusRepository.findById(id).get();
        assertThat(task.getName()).isEqualTo(testTaskStatus.getName());
        assertThat(task.getSlug()).isEqualTo(testTaskStatus.getSlug());
    }

    @Test
    public void testCreateStatus() throws Exception {
        var request = post("/api/task_statuses").with(jwt())
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(testTaskStatus));

        mockMvc.perform(request)
                .andExpect(status().isCreated());

        var task = taskStatusRepository.findBySlug(testTaskStatus.getSlug()).get();
        assertThat(task).isNotNull();
        assertThat(task.getName()).isEqualTo(testTaskStatus.getName());
        assertThat(task.getSlug()).isEqualTo(testTaskStatus.getSlug());
    }

    @Test
    public void testUpdateStatus() throws Exception {
        taskStatusRepository.save(testTaskStatus);
        var id = testTaskStatus.getId();
        var newTaskData = Instancio.of(modelGenerator.getTaskModel()).create();
        var request = put("/api/task_statuses/" + id)
                .with(jwt())
                //.with(jwt().jwt(jwt -> jwt.claim("name", testTaskStatus.getName()).subject(testTaskStatus.getName())))  // an error here to fix
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(newTaskData));

        mockMvc.perform(request)
                .andExpect(status().isOk());

        var task = taskStatusRepository.findById(id).get();
        assertThat(task.getName()).isEqualTo(newTaskData.getName());
    }

    @Test
    public void testDeleteStatus() throws Exception {
        taskStatusRepository.save(testTaskStatus);
        var id = testTaskStatus.getId();

        var request = delete("/api/task_statuses/" + id)
                .with(jwt());

        mockMvc.perform(request)
                .andExpect(status().is(204));

        assertThat(taskStatusRepository.findById(id)).isEmpty();
    }

    @Test
    public void testDeleteUnauthorizedStatus() throws Exception {
        taskStatusRepository.save(testTaskStatus);
        var id = testTaskStatus.getId();

        var request = delete("/api/task_statuses/" + id);
        mockMvc.perform(request)
                .andExpect(status().is(401));

        assertThat(taskStatusRepository.findById(id)).isNotEmpty();
    }


}