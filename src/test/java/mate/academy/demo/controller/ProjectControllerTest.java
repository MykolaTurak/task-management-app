package mate.academy.demo.controller;

import static mate.academy.demo.util.TestUtil.getFirstUser;
import static mate.academy.demo.util.TestUtil.getProjectDto;
import static mate.academy.demo.util.TestUtil.getProjectRequestDto;
import static mate.academy.demo.util.TestUtil.getSecondUser;
import static org.junit.Assert.assertEquals;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDateTime;
import java.util.List;
import mate.academy.demo.dto.project.CreateProjectRequestDto;
import mate.academy.demo.dto.project.ProjectDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class ProjectControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @Sql(scripts = "classpath:db/scripts/create-entities.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:db/scripts/delete-entities.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @DisplayName("""
            save project with valid data
            """)
    void save_WithValidRequest_ShouldReturnValidDto() throws Exception {
        CreateProjectRequestDto projectRequestDto = getProjectRequestDto();
        projectRequestDto.setStartDate(LocalDateTime.of(
                2050, 1, 1, 1, 1, 1));
        projectRequestDto.setEndDate(LocalDateTime.of(
                2050, 1, 1, 1, 1, 1));
        String jsonRequest = objectMapper.writeValueAsString(projectRequestDto);
        ProjectDto expected = getProjectDto();

        MvcResult result = mockMvc.perform(
                        post("/projects")
                                .with(user(getFirstUser()))
                                .content(jsonRequest)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();

        ProjectDto actual = objectMapper.readValue(result.getResponse().getContentAsString(),
                ProjectDto.class);
        expected.setId(actual.getId());
        expected.setStartDate(actual.getStartDate());
        expected.setEndDate(actual.getEndDate());

        assertEquals(expected, actual);
    }

    @Test
    @Sql(scripts = "classpath:db/scripts/create-entities.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:db/scripts/delete-entities.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @DisplayName("""
            save project with invalid data
            """)
    void save_WithInvalidRequest_ShouldReturnBadStatus() throws Exception {
        CreateProjectRequestDto createProjectRequestDto = getProjectRequestDto();
        String jsonRequest = objectMapper.writeValueAsString(createProjectRequestDto);

        mockMvc.perform(
                        post("/projects")
                                .with(user(getFirstUser()))
                                .content(jsonRequest)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest());
    }

    @Sql(scripts = "classpath:db/scripts/create-entities.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:db/scripts/delete-entities.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @DisplayName("""
            find project by id
            """)
    @Test
    void findById_WithValidRequest_ShouldReturnValidDto() throws Exception {
        ProjectDto expected = getProjectDto();

        MvcResult result = mockMvc.perform(
                        get("/projects/1")
                                .with(user(getFirstUser()))
                )
                .andExpect(status().isOk())
                .andReturn();

        ProjectDto actual = objectMapper.readValue(result.getResponse().getContentAsString(),
                ProjectDto.class);

        assertEquals(expected, actual);
    }

    @DisplayName("""
            find project by invalid id
            """)
    @Test
    void findById_WithInvalidRequest_ShouldReturnNotFoundStatus() throws Exception {
        mockMvc.perform(
                        get("/projects/1")
                                .with(user(getFirstUser()))
                )
                .andExpect(status().isNotFound());
    }

    @Test
    @Sql(scripts = "classpath:db/scripts/create-entities.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:db/scripts/delete-entities.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @DisplayName("""
            find all projects
            """)
    void findAll_ShouldReturnValidPage() throws Exception {
        List<ProjectDto> expected = List.of(getProjectDto());

        MvcResult result = mockMvc.perform(
                        get("/projects")
                                .with(user(getFirstUser()))
                )
                .andExpect(status().isOk())
                .andReturn();

        JavaType type = objectMapper.getTypeFactory()
                .constructParametricType(List.class, ProjectDto.class);
        String content = objectMapper.readTree(result.getResponse().getContentAsString())
                .get("content").toString();
        List<ProjectDto> actual = objectMapper.readValue(content, type);

        assertEquals(expected, actual);
    }

    @Test
    @Sql(scripts = "classpath:db/scripts/create-entities.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:db/scripts/delete-entities.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @DisplayName("""
            update label with valid data
            """)
    void update_WithValidRequest_ShouldReturnUpdatedDto() throws Exception {
        CreateProjectRequestDto projectRequestDto = getProjectRequestDto();
        projectRequestDto.setStartDate(LocalDateTime.of(
                2050, 1, 1, 1, 1, 1));
        projectRequestDto.setEndDate(LocalDateTime.of(
                2050, 1, 1, 1, 1, 1));
        String jsonRequest = objectMapper.writeValueAsString(projectRequestDto);
        ProjectDto expected = getProjectDto();
        expected.setStartDate(projectRequestDto.getStartDate());
        expected.setEndDate(projectRequestDto.getEndDate());
        expected.setId(2L);

        MvcResult result = mockMvc.perform(
                        put("/projects/2")
                                .with(user(getSecondUser()))
                                .content(jsonRequest)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();

        ProjectDto actual = objectMapper.readValue(result.getResponse().getContentAsString(),
                ProjectDto.class);

        assertEquals(expected, actual);
    }

    @Test
    @Sql(scripts = "classpath:db/scripts/create-entities.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:db/scripts/delete-entities.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @DisplayName("""
            update label with invalid data
            """)
    void update_WithInvalidRequest_ShouldReturnUpdatedDto() throws Exception {
        String jsonRequest = objectMapper.writeValueAsString(getProjectRequestDto());

        mockMvc.perform(
                        put("/projects/1")
                                .with(user(getSecondUser()))
                                .content(jsonRequest)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("""
            delete by invalid id
            """)
    void delete_WithInvalidId_ShouldReturnOkStatus() throws Exception {
        mockMvc.perform(
                        delete("/projects/1")
                                .with(user(getFirstUser()))
                )
                .andExpect(status().isNotFound());
    }
}
