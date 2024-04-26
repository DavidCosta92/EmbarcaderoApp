package com.Embarcadero.demo.controllers;

import com.Embarcadero.demo.auth.UserRepository;
import com.Embarcadero.demo.auth.entities.AuthResponse;
import com.Embarcadero.demo.auth.entities.RegisterRequest;
import com.Embarcadero.demo.auth.entities.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.*;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.context.WebApplicationContext;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class FilesControllerTest {
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

    final String BASE_URL = "/v1/files/";
    final String BASE_URL_AUTH = "/v1/auth/";
    private String goodUserToken;

    @Autowired
    private WebApplicationContext webApplicationContext;
    @Autowired
    UserRepository userRepository;
    @Autowired

    private MockMvc mockMvc;
    @Autowired
    TestRestTemplate restTemplate;

    @BeforeAll
    void initialSetUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        // registro de good user
        RegisterRequest goodUserRegisterRequest = userRegisterRequest(goodUser);
        ResponseEntity<AuthResponse> responseAuthAdmin = restTemplate.postForEntity(BASE_URL_AUTH +"register", goodUserRegisterRequest,AuthResponse.class);
        AuthResponse authResponse =  responseAuthAdmin.getBody();
        goodUserToken = authResponse.getToken();
    }



    @DisplayName("Carga de imagenes")
    @Test
    @WithMockUser(username = "david") // , roles = {"USER"}
    void uploadUserProfileImg() throws Exception {
        // Obtener el archivo img
        ClassPathResource imageResource = new ClassPathResource("bote.jpeg");
        MockMultipartFile file = new MockMultipartFile("file", "bote.jpeg", "image/jpeg", imageResource.getInputStream());
        User user = userRepository.findByUsername(goodUser.getUsername()).get();
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.multipart(
                BASE_URL + "user/profileImg/" + user.getId())
                        .file(file)
                        .accept(MediaType.APPLICATION_JSON)).andReturn();

        assertTrue(result.getResponse().getStatus()== HttpServletResponse.SC_CREATED);
        assertTrue(result.getResponse().getContentAsString().contains("File upload"));
    }

    @DisplayName("PENDIENTE===>>> Obtener imagen de perfil usuario")
    @Test
    @WithMockUser(username = "david") // , roles = {"USER"}
    void getUserProfileImg() throws Exception {
        /*
        // Obtener el archivo img, y subirlo
        ClassPathResource imageResource = new ClassPathResource("bote.jpeg");
        MockMultipartFile file = new MockMultipartFile("file", "bote.jpeg", "image/jpeg", imageResource.getInputStream());
        User user = userRepository.findByUsername(goodUser.getUsername()).get();
        mockMvc.perform(MockMvcRequestBuilders.multipart(
                        BASE_URL + "user/profileImg/" + user.getId())
                .file(file)
                .accept(MediaType.APPLICATION_JSON));

        // obtener el archivo de imagen del usuario
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get(BASE_URL + "user/profileImg/" + user.getId())
                        .header("Authorization", "Bearer " + goodUserToken)
                        .accept(MediaType.IMAGE_JPEG)).andReturn();
                // .andExpect(content().contentType(MediaType.IMAGE_JPEG)) => error porque me devuelve tipo imagen JPEG+UNnumero aleatorio..

        assertTrue(result.getResponse().getStatus() == HttpServletResponse.SC_OK);
        assertTrue(result.getResponse().getContentAsString().contains(file.getOriginalFilename()));

         */

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