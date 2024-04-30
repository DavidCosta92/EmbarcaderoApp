package com.Embarcadero.demo.controllers;

import com.Embarcadero.demo.auth.UserRepository;
import com.Embarcadero.demo.auth.entities.Role;
import com.Embarcadero.demo.auth.entities.User;
import com.Embarcadero.demo.model.dtos.boat.SimpleBoatAddDto;
import com.Embarcadero.demo.model.dtos.license.LicenseAddDto;
import com.Embarcadero.demo.model.dtos.license.LicenseReadDto;
import com.Embarcadero.demo.model.dtos.license.LicenseUpdateDto;
import com.Embarcadero.demo.model.dtos.person.PersonAddDto;
import com.Embarcadero.demo.model.dtos.records.RecordAddDto;
import com.Embarcadero.demo.model.dtos.records.RecordReadDto;
import com.Embarcadero.demo.model.dtos.records.RecordReadDtoArray;
import com.Embarcadero.demo.model.dtos.records.RecordUpdateDto;
import com.Embarcadero.demo.model.dtos.shift.ShiftAddDto;
import com.Embarcadero.demo.model.dtos.shift.ShiftReadDto;
import com.Embarcadero.demo.model.dtos.user.UserDni;
import com.Embarcadero.demo.model.entities.*;
import com.Embarcadero.demo.model.entities.Record;
import com.Embarcadero.demo.model.entities.boat.RegisteredBoat;
import com.Embarcadero.demo.model.entities.boat.SimpleBoat;
import com.Embarcadero.demo.model.entities.enums.*;
import com.Embarcadero.demo.model.mappers.LicenseMapper;
import com.Embarcadero.demo.model.mappers.PersonMapper;
import com.Embarcadero.demo.model.mappers.RecordMapper;
import com.Embarcadero.demo.model.mappers.UserMapper;
import com.Embarcadero.demo.model.repositories.*;
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
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class RecordControllerTest {
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

    final String BASE_URL = "/v1/records/";
    @Autowired
    private WebApplicationContext webApplicationContext;
    private MockMvc mockMvc;

    // crear usuarios en base de datos..
    User user = new User().builder()
            .username("david1")
            .dni("11111111")
            .email("david1@gmail.com")
            .phone("2644647572")
            .emergencyPhone("2644647572")
            .firstName("david")
            .lastName("costa")
            .password("123456789")
            .role(Role.LIFEGUARD)
            .build();
    User user2 = new User().builder()
            .username("david2")
            .dni("22222222")
            .email("david2@gmail.com")
            .phone("2644647572")
            .emergencyPhone("2644647572")
            .firstName("david")
            .lastName("costa")
            .password("123456789")
            .role(Role.LIFEGUARD)
            .build();

    SimpleBoat simpleBoat = new SimpleBoat().builder()
            .notes("Un bote simple")
            .details("Tdoo ok")
            .typeSimpleBoat(TypeSimpleBoat.WINDSURF)
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
    Record preloadRecord = new Record().builder()
            .car("nxw 678")
            .person(owner)
            .recordState(RecordState.ACTIVO)
            .notes("Regi precargado")
            .startTime(new Date())
            .simpleBoat(simpleBoat)
            .numberOfGuests(0)
            .build();
    Person owner2 = new Person().builder()
            .dni("35924310")
            .notes("dav32421@gmail.com")
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
    RegisteredBoat boat = new RegisteredBoat().builder()
            .id(1)
            .engine(engine)
            .hull("Nauti")
            .name("Bote 1")
            .capacity(5)
            .details("Rotoo ")
            .typeLicencedBoat(TypeLicencedBoat.LANCHA)
            .build();
    License licensePreload1 = new License().builder()
            .code("Msj01")
            .registeredBoat(boat)
            .owner(owner2)
            .state(LicenseState.OK)
            .notes("Notas de licencia")
            .build();

    Record preloadRecord2 = new Record().builder()
            .car("AE735AF")
            .person(owner2)
            .recordState(RecordState.ACTIVO)
            .notes("Regi22 precargado")
            .startTime(new Date())
            .license(licensePreload1)
            .numberOfGuests(3)
            .build();
    Shift preloadShift = Shift.builder()
            .dam(Dam.DIQUE_ULLUM)
            .notes("Guardia de precargada")
            .staff(new ArrayList<>())
            .close(false)
            .date(LocalDate.now())
            .records(new ArrayList<>())
            .build();
    Shift newShift = Shift.builder()
            .dam(Dam.DIQUE_CUESTA_DEL_VIENTO)
            .notes("Guardia de prueba")
            .staff(new ArrayList<>())
            .build();
    Shift preloadShiftToDelete = Shift.builder()
            .dam(Dam.DIQUE_ULLUM)
            .notes("Guardia de precargada para eliminar")
            .staff(new ArrayList<>())
            .close(false)
            .date(LocalDate.now())
            .records(new ArrayList<>())
            .build();

    @Autowired
    UserRepository userRepository;
    @Autowired
    UserMapper userMapper;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private ShiftRepository shiftRepository;
    @Autowired
    private SimpleBoatRepository simpleBoatRepository;
    @Autowired
    PersonRepository personRepository;
    @Autowired
    EngineRepository engineRepository;
    @Autowired
    RegisteredBoatRepository registeredBoatRepository;
    @Autowired
    LicenseRepository licenseRepository;
    @Autowired
    private RecordRepository recordRepository;
    @Autowired
    private PersonMapper personMapper;
    @Autowired
    private LicenseMapper licenseMapper;
    @Autowired
    private RecordMapper recordMapper;

    @BeforeAll
    private void initialSetUp(){
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        // userRepository.save(user);

        // userRepository.save(user2);
        //  newShift.getStaff().add(user); // user


        //simpleBoatRepository.save(simpleBoat); // preloadRecord
        personRepository.save(owner); // preloadRecord

        // records
        personRepository.save(owner2); // preloadRecord2
        engineRepository.save(engine); // preloadRecord2
        registeredBoatRepository.save(boat); // preloadRecord2
        licenseRepository.save(licensePreload1); // preloadRecord2
        recordRepository.save(preloadRecord);
        // recordRepository.save(preloadRecord2);

        // shift
        // preloadShift.getStaff().add(user);
        preloadShift.getRecords().add(preloadRecord);
        // preloadShift.getRecords().add(preloadRecord2);
        shiftRepository.save(preloadShift);
        // shiftRepository.save(preloadShiftToDelete);
    }

    @Test
    @DisplayName("Add a new record")
    @WithMockUser(username = "david", roles = {"LIFEGUARD"})
    void addNewRecord() throws Exception {
        SimpleBoatAddDto simpleBoatAddDto = SimpleBoatAddDto.builder()
                .typeSimpleBoat(preloadRecord.getSimpleBoat().getTypeSimpleBoat())
                .details(preloadRecord.getSimpleBoat().getDetails())
                .notes(preloadRecord.getSimpleBoat().getNotes())
                .build();

        RecordAddDto newRecordAddDto = new RecordAddDto().builder()
                .idShift(1)
                .startTime(preloadRecord.getStartTime())
                .recordState(preloadRecord.getRecordState())
                .person(new PersonAddDto("35924410")) // solo dni, para que sepa que no hay que crear una nueva persona
                .numberOfGuests(preloadRecord.getNumberOfGuests())
                .car(preloadRecord.getCar())
                .hasLicense(false)
                .simpleBoat(simpleBoatAddDto)
                .notes(preloadRecord.getNotes())
                .build();

        MvcResult createRecordResult = mockMvc.perform(MockMvcRequestBuilders.post(BASE_URL)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(mapperStringToJSON(newRecordAddDto))
        ).andReturn();
        String respAsString = createRecordResult.getResponse().getContentAsString();
        ShiftReadDto shiftReadDto = objectMapper.readValue(respAsString, ShiftReadDto.class);

        assertTrue(createRecordResult.getResponse().getStatus() == HttpServletResponse.SC_CREATED);
        assertTrue(shiftReadDto.getRecords().contains(recordMapper.toReadDto(preloadRecord)));

        // Restaurar estado de shift
        preloadShift.getRecords().remove(preloadRecord);
        shiftRepository.save(preloadShift);
    }

    @Test
    @DisplayName("Get all records")
    @WithMockUser(username = "david", roles = {"LIFEGUARD"})
    void findAllRecords() throws Exception {
        MvcResult getAllRecordsResult = mockMvc.perform(MockMvcRequestBuilders.get(BASE_URL)).andReturn();
        String respAsString = getAllRecordsResult.getResponse().getContentAsString();
        RecordReadDtoArray shiftReadDto = objectMapper.readValue(respAsString, RecordReadDtoArray.class);

        assertTrue(getAllRecordsResult.getResponse().getStatus() == HttpServletResponse.SC_OK);
        assertFalse(shiftReadDto.getRecords().isEmpty());
        assertTrue( shiftReadDto.getTotal_results() > 0);
        assertTrue( shiftReadDto.getCurrent_page().equals(0));
    }

    @Test
    @DisplayName("Get record by Id")
    @WithMockUser(username = "david", roles = {"LIFEGUARD"})
    void findById() throws Exception {
        MvcResult getRecordByIdResult = mockMvc.perform(MockMvcRequestBuilders.get(BASE_URL+"1")).andReturn();
        String respAsString = getRecordByIdResult.getResponse().getContentAsString();
        RecordReadDto recordReadDto = objectMapper.readValue(respAsString, RecordReadDto.class);

        assertTrue(getRecordByIdResult.getResponse().getStatus() == HttpServletResponse.SC_OK);
        assertEquals(recordReadDto.getPerson(), personMapper.toReadDto(preloadRecord.getPerson()));
        assertEquals( recordReadDto.getRecordState(), preloadRecord.getRecordState());
        assertEquals( recordReadDto.getSimpleBoat() , recordReadDto.getSimpleBoat());
        assertEquals( recordReadDto.getNotes() , recordReadDto.getNotes());
        assertEquals( recordReadDto.getNumberOfGuests() , recordReadDto.getNumberOfGuests());
        assertEquals( recordReadDto.getStartTime() , recordReadDto.getStartTime());
    }

    @Test
    @DisplayName("Update record by Id")
    @WithMockUser(username = "david", roles = {"LIFEGUARD"})
    void updateRecord() throws Exception {
        String newNotes = "PROBANDO NOTAS";
        String newCar = "AA 123 AA";
        Integer newNumberOfGuests = 4;
        RecordState newState = RecordState.DESCONOCIDO;

        RecordUpdateDto recordUpdate = new RecordUpdateDto().builder()
                .idShift(1)
                .car(newCar)
                .recordState(newState)
                .notes(newNotes)
                .numberOfGuests(newNumberOfGuests)
                .build();

        MvcResult updateRecordResult = mockMvc.perform(MockMvcRequestBuilders.put(BASE_URL+1)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(mapperStringToJSON(recordUpdate))
        ).andReturn();

        // Obtener respuesta como string y mapperlo a clase esperada
        String respAsString = updateRecordResult.getResponse().getContentAsString();
        RecordReadDto recordReadDto = objectMapper.readValue(respAsString, RecordReadDto.class);

        assertEquals(updateRecordResult.getResponse().getStatus(), HttpServletResponse.SC_ACCEPTED);
        assertEquals(recordReadDto.getNotes(), newNotes);
        assertEquals(recordReadDto.getCar(), newCar);
        assertEquals(recordReadDto.getNumberOfGuests(), newNumberOfGuests);
        assertEquals(recordReadDto.getRecordState(), newState);

        // restaurar estado record
        recordUpdate.setNotes(preloadRecord.getNotes());
        recordUpdate.setCar(preloadRecord.getCar());
        recordUpdate.setRecordState(preloadRecord.getRecordState());
        recordUpdate.setNumberOfGuests(preloadRecord.getNumberOfGuests());

        mockMvc.perform(MockMvcRequestBuilders.put(BASE_URL+1)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(mapperStringToJSON(recordUpdate))
        ).andReturn();
    }

    private String mapperStringToJSON(Object object) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(object);
    }
}