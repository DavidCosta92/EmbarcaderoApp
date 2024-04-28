package com.Embarcadero.demo.controllers;

import com.Embarcadero.demo.auth.entities.Role;
import com.Embarcadero.demo.auth.entities.User;
import com.Embarcadero.demo.model.dtos.license.LicenseAddDto;
import com.Embarcadero.demo.model.dtos.license.LicenseReadDto;
import com.Embarcadero.demo.model.dtos.license.LicenseReadDtoArray;
import com.Embarcadero.demo.model.dtos.license.LicenseUpdateDto;
import com.Embarcadero.demo.model.dtos.person.PersonAddDto;
import com.Embarcadero.demo.model.dtos.person.PersonReadDto;
import com.Embarcadero.demo.model.entities.Engine;
import com.Embarcadero.demo.model.entities.License;
import com.Embarcadero.demo.model.entities.Person;
import com.Embarcadero.demo.model.entities.boat.RegisteredBoat;
import com.Embarcadero.demo.model.entities.enums.EngineType;
import com.Embarcadero.demo.model.entities.enums.LicenseState;
import com.Embarcadero.demo.model.entities.enums.TypeLicencedBoat;
import com.Embarcadero.demo.model.mappers.BoatMapper;
import com.Embarcadero.demo.model.mappers.LicenseMapper;
import com.Embarcadero.demo.model.mappers.PersonMapper;
import com.Embarcadero.demo.model.repositories.EngineRepository;
import com.Embarcadero.demo.model.repositories.LicenseRepository;
import com.Embarcadero.demo.model.repositories.PersonRepository;
import com.Embarcadero.demo.model.repositories.RegisteredBoatRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.MediaType;
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
class LicenseControllerTest {

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

    Person owner = new Person().builder()
            .dni("35924410")
            .notes("davidcst29921@gmail.com")
            .phone("2644647572")
            .emergencyPhone("2644647572")
            .name("david")
            .lastName("costa")
            .address("123456789")
            .build();
    Person owner2 = new Person().builder()
            .dni("35924710")
            .notes("davast29921@gmail.com")
            .phone("2644647572")
            .emergencyPhone("2644647572")
            .name("david")
            .lastName("costa")
            .address("123456789")
            .build();

    Engine engine = new Engine().builder()
            .id(1)
            .engineType(EngineType.MOTOR_INTERNO)
            .engineNumber("123")
            .cc("700")
            .notes("motorcillo")
            .build();
    Engine engine2 = new Engine().builder()
            .id(2)
            .engineType(EngineType.MOTOR_INTERNO)
            .engineNumber("456")
            .cc("700")
            .notes("motorcillo 2")
            .build();

    RegisteredBoat boat = new RegisteredBoat().builder()
            .id(1)
            .engine(engine)
            .hull("Nauti")
            .name("Bote 1")
            .capacity(5)
            .details("Rotoo ")
            .typeLicencedBoat(TypeLicencedBoat.LANCHA)
            .build();
    RegisteredBoat boat2 = new RegisteredBoat().builder()
            .id(2)
            .engine(engine2)
            .hull("Lus")
            .name("Bote 2")
            .capacity(3)
            .details("todo bien ")
            .typeLicencedBoat(TypeLicencedBoat.OTROS)
            .build();

    License licensePreload1 = new License().builder()
            .code("Msj01")
            .registeredBoat(boat)
            .owner(owner)
            .state(LicenseState.OK)
            .notes("Notas de licencia")
            .build();
    License licenseToCreate = new License().builder()
            .code("Msj02")
            .registeredBoat(boat2)
            .owner(owner2)
            .state(LicenseState.OK)
            .notes("Notas de licencia")
            .build();


    final String BASE_URL = "/v1/licences/";

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    PersonRepository personRepository;
    @Autowired
    EngineRepository engineRepository;
    @Autowired
    RegisteredBoatRepository registeredBoatRepository;
    @Autowired
    LicenseRepository licenseRepository;
    @Autowired
    PersonMapper personMapper;

    @Autowired
    BoatMapper boatMapper;
    private MockMvc mockMvc;


    @BeforeAll
    void initialSetUp() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        personRepository.save(owner);
        engineRepository.save(engine);
        registeredBoatRepository.save(boat);
        licenseRepository.save(licensePreload1);
    }
    @DisplayName("Create a license")
    @WithMockUser(username = "david", roles = {"SUPER_ADMIN"})
    @Test
    void addLicence() throws Exception {
        LicenseAddDto newLicenseAdd = new LicenseAddDto().builder()
                .code(licenseToCreate.getCode())
                .registeredBoat(boatMapper.toAddDto(licenseToCreate.getRegisteredBoat()))
                .owner(personMapper.toAddDto(licenseToCreate.getOwner()))
                .state(licenseToCreate.getState())
                .notes(licenseToCreate.getNotes())
                .build();

        MvcResult createLicenceResult = mockMvc.perform(MockMvcRequestBuilders.post(BASE_URL)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(mapperStringToJSON(newLicenseAdd))
        ).andReturn();

        // Obtener respuesta como string y mapperlo a clase esperada
        String respAsString = createLicenceResult.getResponse().getContentAsString();
        LicenseReadDto licenceReadDto = objectMapper.readValue(respAsString, LicenseReadDto.class);

        assertTrue(createLicenceResult.getResponse().getStatus() == HttpServletResponse.SC_CREATED);
        assertTrue(licenceReadDto.getCode().equals(licenseToCreate.getCode()));
        assertTrue(licenceReadDto.getState().equals(licenseToCreate.getState()));
        assertTrue(licenceReadDto.getOwner().getDni().equals(licenseToCreate.getOwner().getDni()));
        assertTrue(licenceReadDto.getRegisteredBoat().equals(licenseToCreate.getRegisteredBoat()));
        assertTrue(licenceReadDto.getNotes().equals(licenseToCreate.getNotes()));
    }
    @DisplayName("Find all licenses")
    @WithMockUser(username = "david", roles = {"SUPER_ADMIN"})
    @Test
    void showAll() throws Exception {
        MvcResult findAllLicenseResult = mockMvc.perform(MockMvcRequestBuilders.get(BASE_URL)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
        ).andReturn();
        // Obtener respuesta como string y mapperlo a clase esperada
        String respAsString = findAllLicenseResult.getResponse().getContentAsString();
        LicenseReadDtoArray allLicensesPaginated = objectMapper.readValue(respAsString, LicenseReadDtoArray.class);

        assertTrue(findAllLicenseResult.getResponse().getStatus() == HttpServletResponse.SC_OK);
        assertTrue( !allLicensesPaginated.getLicenses().isEmpty());
        assertTrue( allLicensesPaginated.getTotal_results() > 0);
        assertTrue( allLicensesPaginated.getCurrent_page().equals(0));
    }

    @Test
    @DisplayName("Find a licence by ID")
    @WithMockUser(username = "david", roles = {"SUPER_ADMIN"})
    void showById() throws Exception {
        // No tiene id antes de guardarse en bd, por lo que hardcodeo id 1 asumiento que se crea y borra la bd cada vez que se corre el test
        MvcResult findByCodeResult = mockMvc.perform(MockMvcRequestBuilders.get(BASE_URL+1)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
        ).andReturn();
        // Obtener respuesta como string y mapperlo a clase esperada
        String respAsString = findByCodeResult.getResponse().getContentAsString();
        LicenseReadDto licenseReadDto = objectMapper.readValue(respAsString, LicenseReadDto.class);

        assertEquals(findByCodeResult.getResponse().getStatus() , HttpServletResponse.SC_OK);
        assertEquals(licenseReadDto.getState(), licensePreload1.getState());
        assertEquals(licenseReadDto.getCode(), licensePreload1.getCode());
        assertEquals(licenseReadDto.getOwner(), licensePreload1.getOwner());
        assertEquals(licenseReadDto.getRegisteredBoat(), licensePreload1.getRegisteredBoat());
        assertEquals(licenseReadDto.getNotes(), licensePreload1.getNotes());
    }

    @Test
    @DisplayName("Find a licence by CODE")
    @WithMockUser(username = "david", roles = {"SUPER_ADMIN"})
    void showByCode() throws Exception {
        MvcResult findByCodeResult = mockMvc.perform(MockMvcRequestBuilders.get(BASE_URL+"code/"+licensePreload1.getCode())
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
        ).andReturn();
        // Obtener respuesta como string y mapperlo a clase esperada
        String respAsString = findByCodeResult.getResponse().getContentAsString();
        LicenseReadDto licenseReadDto = objectMapper.readValue(respAsString, LicenseReadDto.class);

        assertEquals(findByCodeResult.getResponse().getStatus() , HttpServletResponse.SC_OK);
        assertEquals(licenseReadDto.getState(), licensePreload1.getState());
        assertEquals(licenseReadDto.getCode(), licensePreload1.getCode());
        assertEquals(licenseReadDto.getOwner(), licensePreload1.getOwner());
        assertEquals(licenseReadDto.getRegisteredBoat(), licensePreload1.getRegisteredBoat());
        assertEquals(licenseReadDto.getNotes(), licensePreload1.getNotes());
    }

    @Test
    @DisplayName("Edit license by ID")
    @WithMockUser(username = "david", roles = {"SUPER_ADMIN"})
    void updateById() throws Exception {
        String newNotes = "PROBANDO NOTAS";
        LicenseState newState = LicenseState.INHABILITADA_TEMPORALMENTE;

        LicenseUpdateDto licenseUpdate = new LicenseUpdateDto().builder()
                .state(newState)
                .notes(newNotes)
                .build();

        MvcResult createLicenceResult = mockMvc.perform(MockMvcRequestBuilders.put(BASE_URL+licensePreload1.getId())
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(mapperStringToJSON(licenseUpdate))
        ).andReturn();

        // Obtener respuesta como string y mapperlo a clase esperada
        String respAsString = createLicenceResult.getResponse().getContentAsString();
        LicenseReadDto licenceReadDto = objectMapper.readValue(respAsString, LicenseReadDto.class);

        assertTrue(createLicenceResult.getResponse().getStatus() == HttpServletResponse.SC_OK);
        assertTrue(licenceReadDto.getCode().equals(licensePreload1.getCode()));
        assertTrue(licenceReadDto.getState().equals(newState));
        assertTrue(licenceReadDto.getNotes().equals(newNotes));

        // restaurar estado licencia
        licenseUpdate.setNotes(licenseToCreate.getNotes());
        licenseUpdate.setState(licenseToCreate.getState());
        mockMvc.perform(MockMvcRequestBuilders.put(BASE_URL+licensePreload1.getId())
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(mapperStringToJSON(licenseUpdate))
        ).andReturn();
    }

    @Test
    @DisplayName("Delete license by ID")
    @WithMockUser(username = "david", roles = {"SUPER_ADMIN"})
    void deleteById() throws Exception {
        MvcResult deleteLicenceResult = mockMvc.perform(MockMvcRequestBuilders.delete(BASE_URL+licensePreload1.getId())).andReturn();

        // Obtener respuesta como string y mapperlo a clase esperada
        String respAsString = deleteLicenceResult.getResponse().getContentAsString();
        LicenseReadDto licenceReadDto = objectMapper.readValue(respAsString, LicenseReadDto.class);

        assertTrue(deleteLicenceResult.getResponse().getStatus() == HttpServletResponse.SC_OK);
        assertTrue(licenceReadDto.getCode().equals(licensePreload1.getCode()));
        assertTrue(licenseRepository.findByCode(licensePreload1.getCode()).isEmpty());

        // restaurar estado licencia
        personRepository.save(owner);
        engineRepository.save(engine);
        registeredBoatRepository.save(boat);
        licenseRepository.save(licensePreload1);
    }

    private String mapperStringToJSON(Object object) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(object);
    }
}