package mate.academy.demo.controller;

import static mate.academy.demo.util.TestUtil.getFirstLabelDto;
import static mate.academy.demo.util.TestUtil.getFirstUser;
import static mate.academy.demo.util.TestUtil.getLabelRequestDto;
import static mate.academy.demo.util.TestUtil.getSecondLabelDto;
import static org.junit.Assert.assertEquals;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import mate.academy.demo.dto.label.CreateLabelRequestDto;
import mate.academy.demo.dto.label.LabelDto;
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
class LabelControllerTest {
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
            save label with valid data
            """)
    void save_WithValidRequest_ShouldReturnValidDto() throws Exception {
        String jsonRequest = objectMapper.writeValueAsString(getLabelRequestDto());
        LabelDto expected = getFirstLabelDto();

        MvcResult result = mockMvc.perform(
                        post("/labels")
                                .with(user(getFirstUser()))
                                .content(jsonRequest)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();

        LabelDto actual = objectMapper.readValue(result.getResponse().getContentAsString(),
                LabelDto.class);
        expected.setId(actual.getId());

        assertEquals(expected, actual);
    }

    @Test
    @Sql(scripts = "classpath:db/scripts/create-entities.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:db/scripts/delete-entities.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @DisplayName("""
            save label with invalid data
            """)
    void save_WithInvalidRequest_ShouldReturnBadStatus() throws Exception {
        CreateLabelRequestDto validCommentRequestDto = getLabelRequestDto();
        validCommentRequestDto.setName(null);
        String jsonRequest = objectMapper.writeValueAsString(validCommentRequestDto);

        mockMvc.perform(
                        post("/labels")
                                .with(user(getFirstUser()))
                                .content(jsonRequest)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    @Sql(scripts = "classpath:db/scripts/create-entities.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:db/scripts/delete-entities.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @DisplayName("""
            find labels by project id
            """)
    void findAllByProjectId_ShouldReturnValidPage() throws Exception {
        List<LabelDto> expected = List.of(getFirstLabelDto(), getSecondLabelDto());

        MvcResult result = mockMvc.perform(
                        get("/labels")
                                .with(user(getFirstUser()))
                                .param("projectId", "1")
                )
                .andExpect(status().isOk())
                .andReturn();

        JavaType type = objectMapper.getTypeFactory()
                .constructParametricType(List.class, LabelDto.class);
        String content = objectMapper.readTree(result.getResponse().getContentAsString())
                .get("content").toString();
        List<LabelDto> actual = objectMapper.readValue(content, type);

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
        String jsonRequest = objectMapper.writeValueAsString(getLabelRequestDto());
        LabelDto expected = getFirstLabelDto();
        expected.setId(getSecondLabelDto().getId());

        MvcResult result = mockMvc.perform(
                        put("/labels/2")
                                .with(user(getFirstUser()))
                                .content(jsonRequest)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();

        LabelDto actual = objectMapper.readValue(result.getResponse().getContentAsString(),
                LabelDto.class);

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
        CreateLabelRequestDto validCommentRequestDto = getLabelRequestDto();
        validCommentRequestDto.setName(null);
        String jsonRequest = objectMapper.writeValueAsString(validCommentRequestDto);

        mockMvc.perform(
                        put("/labels/1")
                                .with(user(getFirstUser()))
                                .content(jsonRequest)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    @Sql(scripts = "classpath:db/scripts/create-entities.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:db/scripts/delete-entities.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @DisplayName("""
            delete by valid id
            """)
    void delete_WithValidId_ShouldReturnOkStatus() throws Exception {
        mockMvc.perform(
                        delete("/labels/1")
                                .with(user(getFirstUser()))
                )
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("""
            delete by invalid id
            """)
    void delete_WithInvalidId_ShouldReturnOkStatus() throws Exception {
        mockMvc.perform(
                        delete("/labels/1")
                                .with(user(getFirstUser()))
                )
                .andExpect(status().isNotFound());
    }
}
