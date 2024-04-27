package com.Embarcadero.demo.controllers;

import com.Embarcadero.demo.auth.UserRepository;
import com.Embarcadero.demo.auth.entities.*;
import com.Embarcadero.demo.model.dtos.person.PersonReadDto;
import com.Embarcadero.demo.model.entities.Person;
import com.Embarcadero.demo.model.repositories.PersonRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwt;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.nio.charset.StandardCharsets;
import java.util.Collection;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class PersonControllerTest {
    // crear usuarios en base de datos..
    User superAdminUser = new User().builder()
            .username("david1")
            .dni("11111111")
            .email("david1@gmail.com")
            .phone("2644647572")
            .emergencyPhone("2644647572")
            .firstName("david")
            .lastName("costa")
            .password("123456789")
            .role(Role.SUPER_ADMIN)
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
    Person personPreload1 = new Person().builder()
            .dni("35924410")
            .notes("davidcst29921@gmail.com")
            .phone("2644i647572")
            .emergencyPhone("26h44647572")
            .name("david")
            .lastName("costa")
            .address("123456789")
            .build();

    final String BASE_URL = "/v1/person/";
    final String BASE_URL_AUTH = "/v1/auth/";
    private String adminToken;
    private String goodUser2Token;


    @Autowired
    private WebApplicationContext webApplicationContext;
    @Autowired
    UserRepository userRepository;
    @Autowired
    PersonRepository personRepository;
    private MockMvc mockMvc;
    @Autowired
    TestRestTemplate restTemplate;


    @BeforeAll
    void initialSetUp() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        personRepository.save(personPreload1);

        // register admin
        RegisterRequest  adminRegisterRequest = userRegisterRequest(superAdminUser);
        ResponseEntity<AuthResponse> responseAuthAdmin = restTemplate.postForEntity(BASE_URL_AUTH+"register", adminRegisterRequest,AuthResponse.class);
        AuthResponse authResponse2 =  responseAuthAdmin.getBody();
        adminToken = authResponse2.getToken();

        /*
        // registro de good user2
        RegisterRequest  goodUser2RegisterRequest = userRegisterRequest(goodUser2);
        ResponseEntity<AuthResponse> responseAuthAdmin2 = restTemplate.postForEntity(BASE_URL+"register", goodUser2RegisterRequest,AuthResponse.class);
        AuthResponse authResponse2 =  responseAuthAdmin2.getBody();
        goodUser2Token = authResponse2.getToken();

         */
    }

    @Test
    @WithMockUser(username = "david", roles = {"SUPER_ADMIN"}) //, roles = {"SUPER_ADMIN"}
    void findByDni() throws Exception {
/*
todo forma 1 restTemplate
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(adminToken);
        HttpEntity<PersonReadDto> requestEntity = new HttpEntity<>(null, headers); // null es el body, que al ser un get es null
        ResponseEntity<PersonReadDto> response = restTemplate.exchange(BASE_URL+ personPreload1.getDni(), HttpMethod.GET, requestEntity, PersonReadDto.class);

        assertThat(response.getBody().getDni()).isEqualTo(personPreload1.getDni());
        assertThat(response.getBody().getName()).isEqualTo(personPreload1.getName());
        assertTrue(response.getStatusCode().is2xxSuccessful());
 */

/*
todo forma 2 MvcResult
 */
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(adminToken);

        MvcResult findByDniResult = mockMvc.perform(MockMvcRequestBuilders.get(BASE_URL+personPreload1.getDni())
                        .headers(headers)
                // .accept(MediaType.APPLICATION_JSON_VALUE)
                // .contentType(MediaType.APPLICATION_JSON_VALUE)
                // .content(mapperStringToJSON(request))
        ).andReturn();

        // assertTrue(newUserResult.getResponse().getContentAsString().contains("token"));

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