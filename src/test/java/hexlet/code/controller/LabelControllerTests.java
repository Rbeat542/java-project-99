package hexlet.code.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import hexlet.code.AppApplication;
import hexlet.code.model.Label;
import hexlet.code.repository.LabelRepository;
import hexlet.code.repository.TaskRepository;
import hexlet.code.repository.TaskStatusRepository;
import hexlet.code.repository.UserRepository;
import hexlet.code.util.ModelGenerator;
import hexlet.code.util.TestKeyGenerator;
import org.instancio.Instancio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import java.nio.charset.StandardCharsets;
import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@AutoConfigureMockMvc
@SpringBootTest(classes = AppApplication.class)
class LabelControllerTests extends TestKeyGenerator {

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

    private Label testLabel;

    @BeforeEach
    public void setUp() {
        modelGenerator.init();

        mockMvc = MockMvcBuilders.webAppContextSetup(wac)
                .defaultResponseCharacterEncoding(StandardCharsets.UTF_8)
                .apply(springSecurity())
                .build();
        testLabel = Instancio.of(modelGenerator.getLabelModel()).create();
    }

    @Test
    public void testIndex() throws Exception {
        labelRepository.save(testLabel);
        var result = mockMvc.perform(get("/api/labels").with(jwt()))
                .andExpect(status().isOk())
                .andReturn();

        var body = result.getResponse().getContentAsString();
        assertThatJson(body).isArray();
        assertThat(body).contains(testLabel.getName());
    }

    @Test
    public void testShowLabel() throws Exception {
        labelRepository.save(testLabel);
        var id = testLabel.getId();
        var request = get("/api/labels/" + id).with(jwt());

        var result = mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn();

        var label = labelRepository.findById(id).get();
        assertThat(label.getName()).isEqualTo(testLabel.getName());
    }

    @Test
    public void testCreateLabel() throws Exception {
        var request = post("/api/labels").with(jwt())
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(testLabel));

        mockMvc.perform(request)
                .andExpect(status().isCreated());

        var label = labelRepository.findByName(testLabel.getName()).get();
        assertThat(label).isNotNull();
        assertThat(label.getName()).isEqualTo(testLabel.getName());
    }

    @Test
    public void testUpdateLabel() throws Exception {
        labelRepository.save(testLabel);
        var id = testLabel.getId();
        var newLabelData = Instancio.of(modelGenerator.getLabelModel()).create();
        var request = put("/api/labels/" + id)
                .with(jwt())
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(newLabelData));

        mockMvc.perform(request)
                .andExpect(status().isOk());

        var label = labelRepository.findById(id).get();
        assertThat(label.getName()).isEqualTo(newLabelData.getName());
    }

    @Test
    public void testDeleteLabel() throws Exception {
        labelRepository.save(testLabel);
        var id = testLabel.getId();

        var request = delete("/api/labels/" + id)
                .with(jwt());

        mockMvc.perform(request)
                .andExpect(status().is(204));

        assertThat(labelRepository.findById(id)).isEmpty();
    }

    @Test
    public void testDeleteUnauthorizedLabel() throws Exception {
        labelRepository.save(testLabel);
        var id = testLabel.getId();

        var request = delete("/api/labels/" + id);
        mockMvc.perform(request)
                .andExpect(status().is(401));

        assertThat(labelRepository.findById(id)).isNotEmpty();
    }
}
