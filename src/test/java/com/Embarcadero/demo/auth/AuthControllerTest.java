package com.Embarcadero.demo.auth;

import com.Embarcadero.demo.auth.entities.*;
import com.Embarcadero.demo.model.dtos.user.UserReadDtoArray;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

// @WebAppConfiguration
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
// @Testcontainers
// @AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AuthControllerTest {

    final String BASE_URL = "/v1/auth/";
    private MockMvc mockMvc;
    private String tokenAdminUser;
    private String goodUserToken;
    private String goodUser2Token;



    @Autowired
    private WebApplicationContext webApplicationContext;
    @Autowired
    TestRestTemplate restTemplate;

    @Autowired
    UserRepository userRepository;

    AuthControllerTest() throws Exception {
    }

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
    User badUser = new User().builder()
            .username("aegws")
            .dni("22222a222")
            .email("david2")
            .phone("2644i647572")
            .emergencyPhone("26h44647572")
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

    @BeforeAll
    void initialSetUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        // userRepository.save(goodUser);

        userRepository.save(userPreload1);

        // registro de good user
        RegisterRequest  goodUserRegisterRequest = userRegisterRequest(goodUser);
        ResponseEntity<AuthResponse> responseAuthAdmin = restTemplate.postForEntity(BASE_URL+"register", goodUserRegisterRequest,AuthResponse.class);
        AuthResponse authResponse =  responseAuthAdmin.getBody();
        goodUserToken = authResponse.getToken();

        // registro de good user2
        RegisterRequest  goodUser2RegisterRequest = userRegisterRequest(goodUser2);
        ResponseEntity<AuthResponse> responseAuthAdmin2 = restTemplate.postForEntity(BASE_URL+"register", goodUser2RegisterRequest,AuthResponse.class);
        AuthResponse authResponse2 =  responseAuthAdmin2.getBody();
        goodUser2Token = authResponse2.getToken();


    }

    @BeforeEach
    void setUp() {


    }
    @AfterEach
    void restoreBd(){


    }

    @AfterAll
    void restoreFinalBd(){
        userRepository.delete(userRepository.findByUsername(userPreload1.getUsername()).get());
        userRepository.delete(userRepository.findByUsername(goodUser2.getUsername()).get());
    }


    @DisplayName("Login Exitoso")
    @Test
    void login() throws Exception {
        LoginRequest goodUserLoginData = new LoginRequest(goodUser.getUsername(),goodUser.getPassword());
        ResponseEntity<AuthResponse> response = restTemplate.postForEntity(BASE_URL+"login", goodUserLoginData,AuthResponse.class);
        assertTrue(response.getStatusCode().isSameCodeAs(HttpStatus.OK));
    }

    @DisplayName("Login error credenciales")
    @Test
    void loginError() throws Exception {
        LoginRequest goodUserLoginData = new LoginRequest(goodUser.getUsername()+"nombreErroneo",goodUser.getPassword());
        ResponseEntity<AuthResponse> response = restTemplate.postForEntity(BASE_URL+"login", goodUserLoginData,AuthResponse.class);
        assertTrue(response.getStatusCode().is4xxClientError());
    }
    @DisplayName("Registro exitoso")
    @Test
    void register() throws Exception {
        User newUser = new User().builder()
                .username("123456")
                .dni("35924888")
                .email("david8881@gmail.com")
                .phone("2644i64888")
                .emergencyPhone("26844647572")
                .firstName("david")
                .lastName("costa")
                .password("123456789")
                .build();

        MvcResult newUserResult = newRegisterUserResult(userRegisterRequest(newUser));
        assertTrue(newUserResult.getResponse().getContentAsString().contains("token"));
    }

    @DisplayName("Registro Error input invalido")
    @Test
    void registerInvalidUser() throws Exception {
        MvcResult badResult = newRegisterUserResult(userRegisterRequest(badUser));
        assertTrue(badResult.getResponse().getContentAsString().contains("Email con formato invalido"));
    }

    @DisplayName("Registro Error ya existente en base de datos")
    @Test
    void registerDuplicatedUser() throws Exception {
        MvcResult badResult = newRegisterUserResult(userRegisterRequest(badUser));
        assertTrue(badResult.getResponse().getContentAsString().contains("Email con formato invalido"));
    }



    @DisplayName("PENDIENTEEEE ")
    @Test
    void getLoguedUserDetails() throws Exception {
        /*
        // registro de good user
        RegisterRequest  goodUser2RegisterRequest = userRegisterRequest(goodUser2);
        ResponseEntity<AuthResponse> responseAuthAdmin = restTemplate.postForEntity(BASE_URL+"register", goodUser2RegisterRequest,AuthResponse.class);
        AuthResponse authResponse =  responseAuthAdmin.getBody();
        goodUser2Token = authResponse.getToken();

        // LoginRequest goodUserLoginData = new LoginRequest(goodUser.getUsername(),goodUser.getPassword());

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(goodUser2Token);

        HttpEntity<String> requestEntity = new HttpEntity<>(null, headers); // null es el body, que al ser un get es null
        ResponseEntity<LoguedUserDetails> response = restTemplate.exchange(BASE_URL+"userDetails", HttpMethod.GET, requestEntity, LoguedUserDetails.class);
        assertThat(response.getBody().getUsername()).isEqualTo(goodUser2.getUsername());
        assertThat(response.getBody().getEmail()).isEqualTo(goodUser2.getEmail());
        assertThat(response.getBody().getDni()).isEqualTo(goodUser2.getDni());
        assertTrue(response.getStatusCode().is2xxSuccessful());

         */
        /*
        tokenAdminUser = newRegisterUserResult(userRegisterRequest(goodUser2)).getResponse().getContentAsString().substring(10,142);
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(tokenAdminUser);

        MvcResult mockMvcResult = mockMvc.perform(MockMvcRequestBuilders.get(BASE_URL+"userDetails")
                .headers(headers)
        ).andReturn();

        MockHttpServletResponse mockMvcResult2 = mockMvcResult.getResponse();

        assertEquals(200, mockMvcResult.getResponse().getStatus());
        assertTrue( mockMvcResult.getResponse().getContentAsString().contains(goodUser2.getUsername()));
        assertTrue( mockMvcResult.getResponse().getContentAsString().contains(goodUser2.getDni()));
        assertTrue( mockMvcResult.getResponse().getContentAsString().contains(goodUser2.getEmail()));
        */

    }
    @DisplayName("PENDIENTEEEE ")
    @Test
    void getAllLifeguards() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(goodUser2Token);
        HttpEntity<String> requestEntity = new HttpEntity<>(null, headers); // null es el body, que al ser un get es null
        ResponseEntity<UserReadDtoArray> response = restTemplate.exchange(BASE_URL+"lifeguards", HttpMethod.GET, requestEntity, UserReadDtoArray.class);
        assertThat(response.getBody().getUsers().size()).isEqualTo(0);
        assertThat(response.getBody().getTotal_results()).isEqualTo(0);
        assertThat(response.getBody().getCurrent_page()).isEqualTo(0);
        assertTrue(response.getStatusCode().is2xxSuccessful());

    }
    @DisplayName("PENDIENTEEEE ")
    @Test
    void getAllUsers() throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(goodUser2Token);

        HttpEntity<String> requestEntity = new HttpEntity<>(null, headers); // null es el body, que al ser un get es null
        ResponseEntity<UserReadDtoArray> response = restTemplate.exchange(BASE_URL+"lifeguards", HttpMethod.GET, requestEntity, UserReadDtoArray.class);
        ResponseEntity<UserReadDtoArray> response2 = restTemplate.exchange(BASE_URL+"users", HttpMethod.GET, requestEntity, UserReadDtoArray.class);

        assertThat(response.getBody().getUsers().size()).isEqualTo(0);
        assertThat(response.getBody().getTotal_results()).isEqualTo(0);
        assertThat(response.getBody().getCurrent_page()).isEqualTo(0);
        assertTrue(response.getStatusCode().is2xxSuccessful());
    }
    @DisplayName("PENDIENTEEEE ")
    @Test
    void editUserRoleById() {
    }
    @DisplayName("PENDIENTEEEE ")
    @Test
    void restorePassword() {
    }
    @DisplayName("PENDIENTEEEE ")
    @Test
    void setNewPassword() {
    }
    @DisplayName("PENDIENTEEEE ")
    @Test
    void editUserDetails() {
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

    // MOCKS


}