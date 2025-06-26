package hexlet.code.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import hexlet.code.mapper.LabelMapper;
import hexlet.code.model.Label;
import hexlet.code.repository.LabelRepository;
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
class LabelControllerTests {

    @Autowired
    LabelMapper LabelMapper;

    @Autowired
    LabelRepository labelRepository;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    private WebApplicationContext wac;

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ModelGenerator modelGenerator;

    private Label testLabel;
    
    @BeforeEach
    public void setUp() {
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
                //.with(jwt().jwt(jwt -> jwt.claim("name", testLabel.getName()).subject(testLabel.getName())))  // an error here to fix
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