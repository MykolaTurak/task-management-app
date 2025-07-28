package mate.academy.demo.controller;

import static mate.academy.demo.util.TestUtil.getCommentDto;
import static mate.academy.demo.util.TestUtil.getFirstUser;
import static mate.academy.demo.util.TestUtil.getValidCommentRequestDto;
import static org.junit.Assert.assertEquals;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import mate.academy.demo.dto.comment.CommentDto;
import mate.academy.demo.dto.comment.CreateCommentRequestDto;
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
class CommentControllerTest {
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
            save comment with valid data
            """)
    void save_WithValidRequest_ShouldReturnValidDto() throws Exception {
        String jsonRequest = objectMapper.writeValueAsString(getValidCommentRequestDto());
        CommentDto expected = getCommentDto();

        MvcResult result = mockMvc.perform(
                        post("/comments")
                                .with(user(getFirstUser()))
                                .content(jsonRequest)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();

        CommentDto actual = objectMapper.readValue(result.getResponse().getContentAsString(),
                CommentDto.class);
        expected.setId(actual.getId());
        expected.setTimestamp(actual.getTimestamp());

        assertEquals(expected, actual);
    }

    @Test
    @Sql(scripts = "classpath:db/scripts/create-entities.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:db/scripts/delete-entities.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @DisplayName("""
            save comment with invalid data
            """)
    void save_WithInvalidRequest_ShouldReturnBadStatus() throws Exception {
        CreateCommentRequestDto validCommentRequestDto = getValidCommentRequestDto();
        validCommentRequestDto.setTaskId(null);
        String jsonRequest = objectMapper.writeValueAsString(validCommentRequestDto);

        mockMvc.perform(
                        post("/comments")
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
            find all comments by task id
            """)
    void findAllByTaskId_ShouldReturnValidPage() throws Exception {
        List<CommentDto> expected = List.of(getCommentDto());

        MvcResult result = mockMvc.perform(
                        get("/comments")
                                .with(user(getFirstUser()))
                                .param("taskId", "1")
                )
                .andExpect(status().isOk())
                .andReturn();

        JavaType type = objectMapper.getTypeFactory()
                .constructParametricType(List.class, CommentDto.class);
        String content = objectMapper.readTree(result.getResponse().getContentAsString())
                .get("content").toString();
        List<CommentDto> actual = objectMapper.readValue(content, type);

        assertEquals(expected, actual);
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
                delete("/comments/1")
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
                        delete("/comments/1")
                                .with(user(getFirstUser()))
                )
                .andExpect(status().isNotFound());
    }
}
