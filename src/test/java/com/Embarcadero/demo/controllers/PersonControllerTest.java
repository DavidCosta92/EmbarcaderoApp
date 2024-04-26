package com.Embarcadero.demo.controllers;

import com.Embarcadero.demo.auth.UserRepository;
import com.Embarcadero.demo.auth.entities.AuthResponse;
import com.Embarcadero.demo.auth.entities.RegisterRequest;
import com.Embarcadero.demo.auth.entities.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class PersonControllerTest {
    // crear usuarios en base de datos..
    User goodUser = new User().builder()
            .username("david1")
            .dni("11111111")
            .email("david1@gmail.com")
            .phone("2644647572")
            .emergencyPhone("2644647572")
            .firstName("david")
            .lastName("costa")
            .password("123456789")
            .build();
    User goodUser2 = new User().builder()
            .username("david2")
            .dni("22222222")
            .email("david2@gmail.com")
            .phone("2644647572")
            .emergencyPhone("2644647572")
            .firstName("david")
            .lastName("costa")
            .password("123456789")
            .build();
    User userPreload1 = new User().builder()
            .username("aegwsdavidCosta")
            .dni("35924410")
            .email("davidcst29921@gmail.com")
            .phone("2644i647572")
            .emergencyPhone("26h44647572")
            .firstName("david")
            .lastName("costa")
            .password("123456789")
            .build();

    final String BASE_URL = "/v1/person/";
    private String goodUserToken;
    private String goodUser2Token;


    @Autowired
    private WebApplicationContext webApplicationContext;
    @Autowired
    UserRepository userRepository;
    private MockMvc mockMvc;
    @Autowired
    TestRestTemplate restTemplate;


    @BeforeAll
    void initialSetUp(){
        userRepository.save(userPreload1);



        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        // registro de good user
        RegisterRequest goodUserRegisterRequest = userRegisterRequest(goodUser);
        ResponseEntity<AuthResponse> responseAuthAdmin = restTemplate.postForEntity(BASE_URL+"register", goodUserRegisterRequest,AuthResponse.class);
        AuthResponse authResponse =  responseAuthAdmin.getBody();
        goodUserToken = authResponse.getToken();

        // registro de good user2
        RegisterRequest  goodUser2RegisterRequest = userRegisterRequest(goodUser2);
        ResponseEntity<AuthResponse> responseAuthAdmin2 = restTemplate.postForEntity(BASE_URL+"register", goodUser2RegisterRequest,AuthResponse.class);
        AuthResponse authResponse2 =  responseAuthAdmin2.getBody();
        goodUser2Token = authResponse2.getToken();
    }

    @Test
    @WithMockUser(username = "david") // , roles = {"USER"}
    void findByDni() {
    }

    @Test
    void showAll() {
    }

    @Test
    void createPerson() {
    }

    @Test
    void updatePerson() {
    }

    // funciones extras
    private RegisterRequest userRegisterRequest(User u){
        return new RegisterRequest().builder()
                .username(u.getUsername())
                .dni(u.getDni())
                .email(u.getEmail())
                .phone(u.getPhone())
                .emergencyPhone(u.getEmergencyPhone())
                .firstName(u.getFirstName())
                .lastName(u.getLastName())
                .password1(u.getPassword())
                .password2(u.getPassword())
                .build();
    }

    private MvcResult newRegisterUserResult(RegisterRequest request) throws Exception {
        return mockMvc.perform(MockMvcRequestBuilders.post(BASE_URL+"register")
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(mapperStringToJSON(request))
        ).andReturn();
    }


    private String mapperStringToJSON(Object object) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(object);
    }
}