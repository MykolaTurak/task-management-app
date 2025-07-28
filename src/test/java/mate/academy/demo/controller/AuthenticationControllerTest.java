package mate.academy.demo.controller;

import static mate.academy.demo.util.TestUtil.getUserDto;
import static mate.academy.demo.util.TestUtil.getUserRequestDto;
import static mate.academy.demo.util.TestUtil.getValidAuthRequestDto;
import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import mate.academy.demo.dto.user.CreateUserRequestDto;
import mate.academy.demo.dto.user.UserDto;
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
class AuthenticationControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @Sql(scripts = "classpath:db/scripts/delete-entities.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @DisplayName("""
            register with valid request
            """)
    void register_WithValidData_ShouldReturnValidDto() throws Exception {
        String jsonRequest = objectMapper.writeValueAsString(getUserRequestDto());
        UserDto expected = getUserDto();

        MvcResult result = mockMvc.perform(
                        post("/auth/registration")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonRequest)

                )
                .andExpect(status().isOk())
                .andReturn();

        UserDto actual = objectMapper.readValue(result.getResponse().getContentAsString(),
                UserDto.class);
        expected.setId(actual.getId());

        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("""
            register with invalid request
            """)
    void register_WithInvalidData_ShouldReturnValidDto() throws Exception {
        CreateUserRequestDto userRequestDto = getUserRequestDto();
        userRequestDto.setEmail(null);
        String jsonRequest = objectMapper.writeValueAsString(userRequestDto);
        UserDto expected = getUserDto();

        mockMvc.perform(
                        post("/auth/registration")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonRequest)

                )
                .andExpect(status().isBadRequest());
    }

    @Test
    @Sql(scripts = "classpath:db/scripts/create-entities.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:db/scripts/delete-entities.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @DisplayName("""
            login with valid request
            """)
    void login_WithValidData_ShouldReturnIsOk() throws Exception {
        String jsonRequest = objectMapper.writeValueAsString(getValidAuthRequestDto());

        mockMvc.perform(
                        post("/auth/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonRequest)

                )
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("""
            login with invalid request
            """)
    void login_WithInvalidData_ShouldReturnIsOk() throws Exception {
        String jsonRequest = objectMapper.writeValueAsString(getValidAuthRequestDto());

        mockMvc.perform(
                        post("/auth/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonRequest)

                )
                .andExpect(status().isNotFound());
    }
}
