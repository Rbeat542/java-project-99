package hexlet.code.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import hexlet.code.dto.taskStatus.TaskStatusCreateDTO;
import hexlet.code.dto.taskStatus.TaskStatusDTO;
import hexlet.code.mapper.TaskStatusMapper;
import hexlet.code.model.TaskStatus;
import hexlet.code.repository.TaskStatusRepository;
import hexlet.code.util.ModelGenerator;
import hexlet.code.util.TestKeyGenerator;
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
class TaskStatusControllerTests extends TestKeyGenerator {

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
    private TaskStatusMapper taskStatusMapper;

    private TaskStatus testStatus;

    @BeforeEach
    public void init() {
        taskStatusRepository.deleteAll();
        modelGenerator.init();

        mockMvc = MockMvcBuilders.webAppContextSetup(wac)
                .defaultResponseCharacterEncoding(StandardCharsets.UTF_8)
                .apply(springSecurity())
                .build();
        testStatus = Instancio.of(modelGenerator.getTaskStatusModel()).create();
        taskStatusRepository.save(testStatus);
    }

    @Test
    public void testIndex() throws Exception {
        var result = mockMvc.perform(get("/api/task_statuses").with(jwt()))
                .andExpect(status().isOk())
                .andReturn();

        var body = result.getResponse().getContentAsString();
        List<TaskStatusDTO> taskStatusDTOS = om.readValue(body, new TypeReference<>() { });

        var expected = taskStatusDTOS;
        var actual = taskStatusRepository.findAll().stream().map(taskStatusMapper::map).toList();
        assertThat(actual).containsExactlyInAnyOrderElementsOf(expected);
    }

    @Test
    public void testShowStatus() throws Exception {
        var id = testStatus.getId();
        var request = get("/api/task_statuses/" + id).with(jwt());

        var result = mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn();

        var body = result.getResponse().getContentAsString();
        assertThatJson(body).and(
                v -> v.node("name").isEqualTo(testStatus.getName()),
                v -> v.node("slug").isEqualTo(testStatus.getSlug()));
    }

    @Test
    public void testCreateStatus() throws Exception {
        var newDTO = new TaskStatusCreateDTO();
        newDTO.setName("NEW NAME");
        newDTO.setSlug("new_slug");

        var request = post("/api/task_statuses").with(jwt())
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(newDTO));

        mockMvc.perform(request)
                .andExpect(status().isCreated());

        var task = taskStatusRepository.findBySlug(newDTO.getSlug()).get();
        assertThat(task.getName()).isEqualTo(newDTO.getName());
        assertThat(task.getSlug()).isEqualTo(newDTO.getSlug());
    }

    @Test
    public void testUpdateStatus() throws Exception {
        var id = testStatus.getId();
        var newTaskData = Instancio.of(modelGenerator.getTaskStatusModel()).create();

        var request = put("/api/task_statuses/" + id)
                .with(jwt())
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(newTaskData));

        mockMvc.perform(request)
                .andExpect(status().isOk());

        var task = taskStatusRepository.findById(id).get();
        assertThat(task.getName()).isEqualTo(newTaskData.getName());
    }

    @Test
    public void testDeleteStatus() throws Exception {
        var id = testStatus.getId();

        var request = delete("/api/task_statuses/" + id)
                .with(jwt());

        mockMvc.perform(request)
                .andExpect(status().is(204));

        assertThat(taskStatusRepository.findById(id)).isEmpty();
    }

    @Test
    public void testDeleteUnauthorizedStatus() throws Exception {
        var id = testStatus.getId();
        var request = delete("/api/task_statuses/" + id);

        mockMvc.perform(request)
                .andExpect(status().is(401));

        assertThat(taskStatusRepository.findById(id)).isNotEmpty();
    }
}
