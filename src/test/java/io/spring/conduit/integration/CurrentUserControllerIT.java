package io.spring.conduit.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.spring.conduit.core.jwt.JwtService;
import io.spring.conduit.core.user.UserRepository;
import io.spring.conduit.model.User;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.Is.isA;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;


import java.util.Optional;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CurrentUserControllerIT {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private JwtService jwtService;

    @Autowired
    private UserRepository userRepository;

    //Test user
    private String mockId = "mockId";
    private String mockUsername = "mockUsername";
    private String mockEmail = "mockEmail@gmail.com";
    private String mockPassword = "P@ssw0rd1234";
    private String mockToken = "token";


    @Test
//    @WithMockCustomUser(
//            id = "mockId",
//            username = "mockUsername",
//            email = "mockEmail@gmail.com",
//            password = "P@ssw0rd1234"
//    )
    public void whenGetCurrentUserAfterAuth_ShouldReturnCurrentUser() throws Exception {
        //given
        User currentUser = User.builder()
                .username(mockUsername)
                .password(mockPassword)
                .email(mockEmail)
                .bio("mock bio")
                .image("mock image")
                .build();
        User saveUser = userRepository.saveAndFlush(currentUser);
        when(jwtService.getSubjectFromToken(mockToken)).thenReturn(Optional.of(saveUser.getId()));

        //when
        ResultActions resultActions = mockMvc.perform(get("/user")
                .header("Authorization", "Token " + mockToken)
                .contentType("application/json"));

        //then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.user.email",is(mockEmail)))
                .andExpect(jsonPath("$.user.token",isA(String.class)))
                .andExpect(jsonPath("$.user.username",is(mockUsername)))
                .andExpect(jsonPath("$.user.bio", isA(String.class)))
                .andExpect(jsonPath("$.user.image", isA(String.class)));
    }

}
