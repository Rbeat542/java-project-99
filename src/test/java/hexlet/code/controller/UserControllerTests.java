package hexlet.code.controller;

import hexlet.code.mapper.UserMapper;
import hexlet.code.model.User;
import hexlet.code.repository.LabelRepository;
import hexlet.code.repository.TaskRepository;
import hexlet.code.repository.TaskStatusRepository;
import hexlet.code.repository.UserRepository;
import hexlet.code.util.ModelGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;
import static org.assertj.core.api.Assertions.assertThat;
import org.instancio.Instancio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;

import java.nio.charset.StandardCharsets;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;

import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTests {

    @Autowired
    LabelRepository labelRepository;

    @Autowired
    TaskRepository taskRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    TaskStatusRepository taskStatusRepository;

    @Autowired
    UserMapper userMapper;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    private WebApplicationContext wac;

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ModelGenerator modelGenerator;

    private User testUser;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    public void setUp() {
        modelGenerator.init();

        mockMvc = MockMvcBuilders.webAppContextSetup(wac)
                .defaultResponseCharacterEncoding(StandardCharsets.UTF_8)
                .apply(springSecurity())
                .build();
        testUser = Instancio.of(modelGenerator.getUserModel()).create();
    }

    @Test
    public void testIndex() throws Exception {
        userRepository.save(testUser);
        var result = mockMvc.perform(get("/api/users").with(jwt()))
                .andExpect(status().isOk())
                .andReturn();

        var body = result.getResponse().getContentAsString();
        assertThatJson(body).isArray();
    }

    @Test
    public void testShowUser() throws Exception {
        userRepository.save(testUser);
        var id = testUser.getId();
        var request = get("/api/users/" + id).with(jwt());

        var result = mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn();
        //var body = result.getResponse().getContentAsString();

        var user = userRepository.findById(id).get();
        assertThat(user.getEmail()).isEqualTo(testUser.getEmail());
        assertThat(user.getPassword()).isEqualTo(testUser.getPassword());

        /*assertThatJson(body).and(
                        v -> v.node("firstName").isEqualTo(testUser.getFirstName()),
                        v -> v.node("email").isEqualTo(testUser.getEmail()));*/

    }

    @Test
    public void testCreateUser() throws Exception {
        var request = post("/api/users").with(jwt())
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(testUser));

        mockMvc.perform(request)
                .andExpect(status().isCreated());

        var user = userRepository.findByEmail(testUser.getUsername()).get();
        assertThat(user).isNotNull();
        assertThat(user.getFirstName()).isEqualTo(testUser.getFirstName());
        assertThat(user.getLastName()).isEqualTo(testUser.getLastName());
        assertThat(passwordEncoder.matches(testUser.getPassword(), user.getPassword())).isTrue();
    }

    @Test
    public void testCreateUserWithInvalidPassword() throws Exception {
        testUser.setPasswordDigest("22");
        var request = post("/api/users").with(jwt())
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(testUser));

        mockMvc.perform(request)
                .andExpect(status().isBadRequest());

        assertThat(userRepository.findByEmail(testUser.getUsername()).isEmpty());
    }


    @Test
    public void testUpdateUser() throws Exception {
        userRepository.save(testUser);
        var newUserData = Instancio.of(modelGenerator.getUserModel()).create();
        var id = testUser.getId();
        var request = put("/api/users/" + id)
                .with(jwt().jwt(jwt -> jwt.claim("email", testUser.getEmail())
                        .subject(testUser.getEmail())))
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(newUserData));

        mockMvc.perform(request)
                .andExpect(status().isOk());

        var user = userRepository.findById(id).get();
        assertThat(user.getFirstName()).isEqualTo(newUserData.getFirstName());
        assertThat(user.getLastName()).isEqualTo(newUserData.getLastName());
        assertThat(user.getEmail()).isEqualTo(newUserData.getEmail());
    }

    @Test
    public void testDeleteUser() throws Exception {
        userRepository.save(testUser);
        var id = testUser.getId();

        var request = delete("/api/users/" + id)
                .with(jwt().jwt(jwt -> jwt.claim("email", testUser.getEmail())
                        .subject(testUser.getEmail())));

        mockMvc.perform(request)
                .andExpect(status().isOk());

        assertThat(userRepository.findById(id)).isEmpty();
    }


    @Test
    public void testDeleteWrongUser() throws Exception {
        userRepository.save(testUser);
        var admin = userRepository.findByEmail("hexlet@example.com").get();
        var id = admin.getId();
        var request = delete("/api/users/" + id)
                .with(jwt().jwt(jwt -> jwt.claim("email", testUser.getEmail()).subject(testUser.getEmail())));
        mockMvc.perform(request)
                .andExpect(status().is(403));

        assertThat(userRepository.findById(id)).isNotEmpty();
    }

    @Test
    public void testDeleteUnauthorizedUser() throws Exception {
        userRepository.save(testUser);
        var id = testUser.getId();
        var request = delete("/api/users/" + id);

        mockMvc.perform(request)
                .andExpect(status().is(401));

        assertThat(userRepository.findById(id)).isNotEmpty();
    }
}
