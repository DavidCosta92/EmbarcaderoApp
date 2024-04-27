package com.Embarcadero.demo.controllers;

import com.Embarcadero.demo.auth.UserRepository;
import com.Embarcadero.demo.auth.entities.*;
import com.Embarcadero.demo.model.dtos.person.PersonAddDto;
import com.Embarcadero.demo.model.dtos.person.PersonReadDto;
import com.Embarcadero.demo.model.dtos.person.PersonReadDtoArray;
import com.Embarcadero.demo.model.dtos.person.PersonUpdateDto;
import com.Embarcadero.demo.model.entities.Person;
import com.Embarcadero.demo.model.mappers.PersonMapper;
import com.Embarcadero.demo.model.repositories.PersonRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwt;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.mock.web.MockHttpServletResponse;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class PersonControllerTest {

/*
 todo AUTH - agregar headers con auth
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(adminToken);

 todo AUTH - @WithMockUser(username = "david", roles = {"SUPER_ADMIN"}) //, roles = {"SUPER_ADMIN"}

 todo - forma 1 restTemplate
        HttpEntity<PersonReadDto> requestEntity = new HttpEntity<>(null, headers); // null es el body, que al ser un get es null
        ResponseEntity<PersonReadDto> response = restTemplate.exchange(BASE_URL+ personPreload1.getDni(), HttpMethod.GET, requestEntity, PersonReadDto.class);

 todo - forma 2 restTemplate
         MvcResult findByDniResult = mockMvc.perform(MockMvcRequestBuilders.get(BASE_URL+personPreload1.getDni())
                        .headers(headers)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        ).andReturn();
        // Obtener respuesta como string y mapperlo a clase esperada
        String respAsString = findByDniResult.getResponse().getContentAsString();
        PersonReadDto personReadDto = objectMapper.readValue(respAsString, PersonReadDto.class);
 */

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
            .phone("2644647572")
            .emergencyPhone("2644647572")
            .name("david")
            .lastName("costa")
            .address("123456789")
            .build();
    Person personPreload2= new Person().builder()
            .dni("31924410")
            .notes("dav12@gmail.com")
            .phone("2644647572")
            .emergencyPhone("2644647572")
            .name("dassvid")
            .lastName("cossdsta")
            .address("123456789")
            .build();
    Person personToCreate = new Person().builder()
            .dni("35924411")
            .notes("da12@gmail.com")
            .phone("2644647572")
            .emergencyPhone("2644647572")
            .name("davidC")
            .lastName("costaas")
            .address("123456789")
            .build();
    final String BASE_URL = "/v1/person/";
    final String BASE_URL_AUTH = "/v1/auth/";
    private String adminToken;
    private String goodUser2Token;

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private PersonMapper personMapper;
    @Autowired
    private WebApplicationContext webApplicationContext;
    @Autowired
    PersonRepository personRepository;
    private MockMvc mockMvc;
    @Autowired
    TestRestTemplate restTemplate;


    @BeforeAll
    void initialSetUp() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        personRepository.save(personPreload1);
        personRepository.save(personPreload2);

        // register admin
        RegisterRequest  adminRegisterRequest = userRegisterRequest(superAdminUser);
        ResponseEntity<AuthResponse> responseAuthAdmin = restTemplate.postForEntity(BASE_URL_AUTH+"register", adminRegisterRequest,AuthResponse.class);
        AuthResponse authResponse2 =  responseAuthAdmin.getBody();
        adminToken = authResponse2.getToken();
    }

    @Test
    @DisplayName("Find a person by DNI")
    @WithMockUser(username = "david", roles = {"SUPER_ADMIN"})
    void findByDni() throws Exception {
        MvcResult findByDniResult = mockMvc.perform(MockMvcRequestBuilders.get(BASE_URL+personPreload1.getDni())
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        ).andReturn();
        // Obtener respuesta como string y mapperlo a clase esperada
        String respAsString = findByDniResult.getResponse().getContentAsString();
        PersonReadDto personReadDto = objectMapper.readValue(respAsString, PersonReadDto.class);

        assertEquals(findByDniResult.getResponse().getStatus() , HttpServletResponse.SC_OK);
        assertEquals(personReadDto.getDni(), personPreload1.getDni());
        assertEquals(personReadDto.getName(), personPreload1.getName());
        assertEquals(personReadDto.getLastName(), personPreload1.getLastName());
        assertEquals(personReadDto.getPhone(), personPreload1.getPhone());
        assertEquals(personReadDto.getEmergencyPhone(), personPreload1.getEmergencyPhone());
        assertEquals(personReadDto.getAddress(), personPreload1.getAddress());
        assertEquals(personReadDto.getNotes(), personPreload1.getNotes());
    }

    @DisplayName("Find all persons")
    @WithMockUser(username = "david", roles = {"SUPER_ADMIN"})
    @Test
    void showAll() throws Exception {
        MvcResult findAllPersonResult = mockMvc.perform(MockMvcRequestBuilders.get(BASE_URL)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
        ).andReturn();

        // Obtener respuesta como string y mapperlo a clase esperada
        String respAsString = findAllPersonResult.getResponse().getContentAsString();
        PersonReadDtoArray allPersonsPaginated = objectMapper.readValue(respAsString, PersonReadDtoArray.class);

        assertTrue(findAllPersonResult.getResponse().getStatus() == HttpServletResponse.SC_OK);
        assertTrue( !allPersonsPaginated.getPersons().isEmpty());
        assertTrue( allPersonsPaginated.getTotal_results() > 0);
        assertTrue( allPersonsPaginated.getCurrent_page().equals(0));
    }

    @DisplayName("Create new person")
    @WithMockUser(username = "david", roles = {"SUPER_ADMIN"})
    @Test
    void createPerson() throws Exception {
        PersonAddDto newPersonAdd = personMapper.toAddDto(personToCreate);

        MvcResult createPersonResult = mockMvc.perform(MockMvcRequestBuilders.post(BASE_URL)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(mapperStringToJSON(newPersonAdd))
        ).andReturn();

        // Obtener respuesta como string y mapperlo a clase esperada
        String respAsString = createPersonResult.getResponse().getContentAsString();
        PersonReadDto personReadDto = objectMapper.readValue(respAsString, PersonReadDto.class);

        assertTrue(createPersonResult.getResponse().getStatus() == HttpServletResponse.SC_CREATED);
        assertTrue(personReadDto.getDni().equals(personToCreate.getDni()));
        assertTrue(personReadDto.getName().equals(personToCreate.getName()));
        assertTrue(personReadDto.getLastName().equals(personToCreate.getLastName()));
        assertTrue(personReadDto.getPhone().equals(personToCreate.getPhone()));
        assertTrue(personReadDto.getEmergencyPhone().equals(personToCreate.getEmergencyPhone()));
        assertTrue(personReadDto.getNotes().equals(personToCreate.getNotes()));
    }

    @DisplayName("Update a person")
    @WithMockUser(username = "david", roles = {"SUPER_ADMIN"})
    @Test
    void updatePerson() throws Exception {
        String newName = "Otro nombre";
        String newLastName = "Otro apellidoo";
        PersonUpdateDto updateDto = new PersonUpdateDto().builder()
                .dni(personPreload2.getDni())
                .name(newName)
                .lastName(newLastName)
                .build();

        MvcResult updateResult = mockMvc.perform(MockMvcRequestBuilders.put(BASE_URL)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(mapperStringToJSON(updateDto))
        ).andReturn();

        // Obtener respuesta como string y mapperlo a clase esperada
        String respAsString = updateResult.getResponse().getContentAsString();
        PersonReadDto personReadDto = objectMapper.readValue(respAsString, PersonReadDto.class);

        assertEquals(updateResult.getResponse().getStatus() , HttpServletResponse.SC_ACCEPTED);
        assertEquals(personReadDto.getDni() , personPreload2.getDni());
        assertEquals(personReadDto.getName() , newName);
        assertEquals(personReadDto.getLastName() , newLastName);
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

    private String mapperStringToJSON(Object object) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(object);
    }
}