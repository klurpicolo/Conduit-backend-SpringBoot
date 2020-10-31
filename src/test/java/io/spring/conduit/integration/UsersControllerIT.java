package io.spring.conduit.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.spring.conduit.helper.SecurityContextFacade;
import io.spring.conduit.core.user.UserRepository;
import io.spring.conduit.dto.request.LoginUserRq;
import io.spring.conduit.dto.request.RegisterUserRq;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.Is.isA;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UsersControllerIT {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private SecurityContextFacade securityContextFacade;

    //Test user
    private String username = "RulkTiraw";
    private String email = "RulkTiraw@gmail.com";
    private String password = "P@ssw0rd1234";

    @Test
    @Order(1)
    void whenRegisterValidUser_ThenReturnRegisteredUser() throws Exception {
        //given
        RegisterUserRq registerUserRq = RegisterUserRq.builder()
                .username(username)
                .email(email)
                .password(password).build();

        Map<String,Object> request = new HashMap<String, Object>() {{
            put("user", registerUserRq);
        }};

        //when
        ResultActions resultActions = mockMvc.perform(post("/users")
                .contentType("application/json")
                .content(objectMapper.writeValueAsBytes(request)));

        //then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.user.email", is(email)))
                .andExpect(jsonPath("$.user.username",is(username)))
                .andExpect(jsonPath("$.user.bio", isA(String.class)))
                .andExpect(jsonPath("$.user.image", isA(String.class)));
        assertEquals(email, userRepository.findByEmail(email).get().getEmail());
    }

    @Test
    @Order(2)
    void whenLoginRegisteredUser_ThenReturnCurrentUser() throws Exception {
        //given
        LoginUserRq loginUserRq = LoginUserRq.builder()
                .email(email)
                .password(password).build();

        Map<String,Object> request = new HashMap<String, Object>() {{
            put("user", loginUserRq);
        }};

        //when
        ResultActions resultActions = mockMvc.perform(post("/users/login")
                .contentType("application/json")
                .content(objectMapper.writeValueAsBytes(request)));

        //then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.user.email", is(email)))
                .andExpect(jsonPath("$.user.username",is(username)))
                .andExpect(jsonPath("$.user.bio", isA(String.class)))
                .andExpect(jsonPath("$.user.image", isA(String.class)));
    }

    @Test
    @Order(3)
    void whenLoginUnRegisterUser_ThenReturnError() throws Exception {
        //given
        String UnregisteredEmail = "poasd@gmail.com";
        String UnregisteredPassword = "mmmbbbkkkbbb";
        LoginUserRq loginUserRq = LoginUserRq.builder()
                .email(UnregisteredEmail)
                .password(UnregisteredPassword).build();

        Map<String,Object> request = new HashMap<String, Object>() {{
            put("user", loginUserRq);
        }};

        //when
        ResultActions resultActions = mockMvc.perform(post("/users/login")
                .contentType("application/json")
                .content(objectMapper.writeValueAsBytes(request)));

        //then
        resultActions
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.password[0]", is("invalid email or password!!!")));

    }

}
