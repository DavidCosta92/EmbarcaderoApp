package com.Embarcadero.demo.controllers;

import com.Embarcadero.demo.auth.UserRepository;
import com.Embarcadero.demo.auth.entities.Role;
import com.Embarcadero.demo.auth.entities.User;
import com.Embarcadero.demo.model.dtos.license.LicenseAddDto;
import com.Embarcadero.demo.model.dtos.license.LicenseReadDto;
import com.Embarcadero.demo.model.dtos.license.LicenseUpdateDto;
import com.Embarcadero.demo.model.dtos.records.RecordReadDto;
import com.Embarcadero.demo.model.dtos.shift.ShiftAddDto;
import com.Embarcadero.demo.model.dtos.shift.ShiftReadDto;
import com.Embarcadero.demo.model.dtos.shift.ShiftReadDtoArray;
import com.Embarcadero.demo.model.dtos.shift.ShiftUpdateDto;
import com.Embarcadero.demo.model.dtos.staff.StaffMemberAddDto;
import com.Embarcadero.demo.model.dtos.user.UserDni;
import com.Embarcadero.demo.model.dtos.user.UserReadDto;
import com.Embarcadero.demo.model.dtos.user.UserStaffReadDto;
import com.Embarcadero.demo.model.entities.*;
import com.Embarcadero.demo.model.entities.Record;
import com.Embarcadero.demo.model.entities.boat.RegisteredBoat;
import com.Embarcadero.demo.model.entities.boat.SimpleBoat;
import com.Embarcadero.demo.model.entities.enums.*;
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
class ShiftControllerTest {
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
    private WebApplicationContext webApplicationContext;
    private MockMvc mockMvc;
    final String BASE_URL = "/v1/shifts/";
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


    @BeforeAll
    private void initialSetUp(){
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        userRepository.save(user);
        userRepository.save(user2);
        newShift.getStaff().add(user); // user


        //simpleBoatRepository.save(simpleBoat); // preloadRecord
        personRepository.save(owner); // preloadRecord

        // records
        personRepository.save(owner2); // preloadRecord2
        engineRepository.save(engine); // preloadRecord2
        registeredBoatRepository.save(boat); // preloadRecord2
        licenseRepository.save(licensePreload1); // preloadRecord2
        recordRepository.save(preloadRecord);
        recordRepository.save(preloadRecord2);

        // shift
        preloadShift.getStaff().add(user);
        preloadShift.getRecords().add(preloadRecord);
        preloadShift.getRecords().add(preloadRecord2);
        shiftRepository.save(preloadShift);
        shiftRepository.save(preloadShiftToDelete);
    }

    @Test
    @DisplayName("Create a new shift")
    @WithMockUser(username = "david", roles = {"LIFEGUARD"})
    void addNewShift() throws Exception {
        List<UserDni> staffToAdd = new ArrayList<>();
        staffToAdd.add(new UserDni(user.getDni()));

        ShiftAddDto newShiftToAdd = new ShiftAddDto().builder()
                .dam(newShift.getDam())
                .notes(newShift.getNotes())
                .staff(staffToAdd)
                .build();

        MvcResult createShiftResult = mockMvc.perform(MockMvcRequestBuilders.post(BASE_URL)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(mapperStringToJSON(newShiftToAdd))
        ).andReturn();

        // Obtener respuesta como string y mapperlo a clase esperada
        String respAsString = createShiftResult.getResponse().getContentAsString();
        ShiftReadDto shiftReadDto = objectMapper.readValue(respAsString, ShiftReadDto.class);

        assertTrue(createShiftResult.getResponse().getStatus() == HttpServletResponse.SC_CREATED);
        assertTrue(shiftReadDto.getDam().equals(newShift.getDam()));
        assertTrue(shiftReadDto.getStaff().equals(newShift.getStaff().stream().map(staff -> userMapper.toReadDto(staff)).collect(Collectors.toList())));
        assertTrue(shiftReadDto.getNotes().equals(newShift.getNotes()));
        assert(shiftReadDto.getDate()).equals(LocalDate.now());
        assertTrue(shiftReadDto.getRecords().isEmpty());
        assertTrue(!shiftReadDto.getClose());
    }

    @Test
    @DisplayName("Get all shifts paginated")
    @WithMockUser(username = "david", roles = {"LIFEGUARD"})
    void showAll() throws Exception {

        MvcResult getAllShiftResult = mockMvc.perform(MockMvcRequestBuilders.get(BASE_URL)).andReturn();

        // Obtener respuesta como string y mapperlo a clase esperada
        String respAsString = getAllShiftResult.getResponse().getContentAsString();
        ShiftReadDtoArray shiftReadDtoArray = objectMapper.readValue(respAsString, ShiftReadDtoArray.class);

        assertTrue(getAllShiftResult.getResponse().getStatus() == HttpServletResponse.SC_OK);
        assertFalse(shiftReadDtoArray.getShifts().isEmpty());
        assertTrue( shiftReadDtoArray.getTotal_results() > 0);
        assertTrue( shiftReadDtoArray.getCurrent_page().equals(0));

    }

    @Test
    @DisplayName("Get shift by Id")
    @WithMockUser(username = "david", roles = {"LIFEGUARD"})
    void findById() throws Exception {
        // No tiene id antes de guardarse en bd, por lo que hardcodeo id 1 asumiento que se crea y borra la bd cada vez que se corre el test
        MvcResult findByIdResult = mockMvc.perform(MockMvcRequestBuilders.get(BASE_URL+1)).andReturn();
        // Obtener respuesta como string y mapperlo a clase esperada
        String respAsString = findByIdResult.getResponse().getContentAsString();
        ShiftReadDto shiftReadDto = objectMapper.readValue(respAsString, ShiftReadDto.class);

        assertEquals(findByIdResult.getResponse().getStatus() , HttpServletResponse.SC_OK);
        assertEquals(shiftReadDto.getDate(), preloadShift.getDate());
        assertEquals(shiftReadDto.getDam(), preloadShift.getDam());
        assertEquals(shiftReadDto.getClose(), preloadShift.getClose());
        assertEquals(shiftReadDto.getNotes(), preloadShift.getNotes());
    }

    @Test
    @DisplayName("Get active shift by Id USER")
    @WithMockUser(username = "david", roles = {"LIFEGUARD"})
    void findShiftByIdUser() throws Exception {
        // No tiene id antes de guardarse en bd, por lo que hardcodeo id 1 ya que se crea y borra la bd cada vez que se corre el test
        MvcResult findByIdUserResult = mockMvc.perform(MockMvcRequestBuilders.get(BASE_URL+"user/1")).andReturn();
        String respAsString = findByIdUserResult.getResponse().getContentAsString();
        ShiftReadDto shiftReadDto = objectMapper.readValue(respAsString, ShiftReadDto.class);

        assertEquals(findByIdUserResult.getResponse().getStatus() , HttpServletResponse.SC_OK);
        assertEquals(shiftReadDto.getDate(), preloadShift.getDate());
        assertEquals(shiftReadDto.getDam(), preloadShift.getDam());
        assertEquals(shiftReadDto.getClose(), preloadShift.getClose());
        assertEquals(shiftReadDto.getNotes(), preloadShift.getNotes());
    }

    @Test
    @DisplayName("Delete a shift by ID")
    @WithMockUser(username = "david", roles = {"LIFEGUARD"})
    void deleteShift() throws Exception {
        // No tiene id antes de guardarse en bd, por lo que hardcodeo id 2 ya que se crea y borra la bd cada vez que se corre el test, y este se guarda en segundo lugar
        MvcResult deleteShiftResult = mockMvc.perform(MockMvcRequestBuilders.delete(BASE_URL+2)).andReturn();

        String respAsString = deleteShiftResult.getResponse().getContentAsString();
        ShiftReadDto shiftReadDto = objectMapper.readValue(respAsString, ShiftReadDto.class);

        assertTrue(deleteShiftResult.getResponse().getStatus() == HttpServletResponse.SC_ACCEPTED);
        assertTrue(shiftReadDto.getDate().equals(preloadShiftToDelete.getDate()));
        assertTrue(shiftReadDto.getDam().equals(preloadShiftToDelete.getDam()));
        assertTrue(shiftReadDto.getNotes().equals(preloadShiftToDelete.getNotes()));
    }

    @Test
    @DisplayName("Edit shift by ID")
    @WithMockUser(username = "david", roles = {"LIFEGUARD"})
    void updateShift() throws Exception {
        String newNotes = "PROBANDO NOTAS";
        Dam newDam = Dam.DIQUE_PUNTA_NEGRA;

        ShiftUpdateDto shiftUpdate = new ShiftUpdateDto().builder()
                .notes(newNotes)
                .dam(newDam)
                .build();

        MvcResult updateResult = mockMvc.perform(MockMvcRequestBuilders.put(BASE_URL+1)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(mapperStringToJSON(shiftUpdate))
        ).andReturn();
        String respAsString = updateResult.getResponse().getContentAsString();
        ShiftReadDto shiftReadDto = objectMapper.readValue(respAsString, ShiftReadDto.class);

        assertTrue(updateResult.getResponse().getStatus() == HttpServletResponse.SC_ACCEPTED);
        assertTrue(shiftReadDto.getNotes().equals(newNotes));
        assertTrue(shiftReadDto.getDam().equals(newDam));

        // restaurar estado original shift
        shiftUpdate.setNotes(preloadShift.getNotes());
        shiftUpdate.setDam(preloadShift.getDam());
        mockMvc.perform(MockMvcRequestBuilders.put(BASE_URL+1)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(mapperStringToJSON(shiftUpdate))
        ).andReturn();
    }

    @Test
    @DisplayName("Add staff")
    @WithMockUser(username = "david", roles = {"LIFEGUARD"})
    void addStaffToShift() throws Exception {
        StaffMemberAddDto staffToAdd = new StaffMemberAddDto(user2.getDni());

        MvcResult AddStaffResult = mockMvc.perform(MockMvcRequestBuilders.post(BASE_URL+1+"/staff/")
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(mapperStringToJSON(staffToAdd))
        ).andReturn();

        String respAsString = AddStaffResult.getResponse().getContentAsString();
        ShiftReadDto shiftReadDto = objectMapper.readValue(respAsString, ShiftReadDto.class);

        assertEquals(AddStaffResult.getResponse().getStatus(), HttpServletResponse.SC_ACCEPTED);
        assertFalse(shiftReadDto.getStaff().isEmpty());
        assertTrue(shiftReadDto.getStaff().contains(userMapper.toReadDto(user2)));

        // restaurar estado de staff
        preloadShift.getStaff().remove(user2);
        shiftRepository.save(preloadShift);
    }

    @Test
    @DisplayName("Remove staff")
    @WithMockUser(username = "david", roles = {"LIFEGUARD"})
    void removeStaffFromShift() throws Exception {
        MvcResult RemoveStaffResult = mockMvc.perform(MockMvcRequestBuilders.put(BASE_URL+1+"/staff/"+1)).andReturn();  //{idShift}/staff/{idStaff}

        String respAsString = RemoveStaffResult.getResponse().getContentAsString();
        ShiftReadDto shiftReadDto = objectMapper.readValue(respAsString, ShiftReadDto.class);

        assertEquals(RemoveStaffResult.getResponse().getStatus(), HttpServletResponse.SC_ACCEPTED);
        assertFalse(shiftReadDto.getStaff().contains(userMapper.toReadDto(user)));

        // restaurar estado de staff
        preloadShift.getStaff().add(user);
        shiftRepository.save(preloadShift);
    }

    @Test
    @DisplayName("Download shift resume")
    @WithMockUser(username = "david", roles = {"LIFEGUARD"})
    void downloadShiftResume() throws Exception {
        MvcResult downloadShiftResumeResult = mockMvc.perform(MockMvcRequestBuilders.get(BASE_URL+"shiftResume/"+1)).andReturn();
        assertEquals(downloadShiftResumeResult.getResponse().getStatus() , HttpServletResponse.SC_OK);
        assertEquals(downloadShiftResumeResult.getResponse().getContentType() , MediaType.APPLICATION_PDF_VALUE);
        assertFalse(downloadShiftResumeResult.getResponse().getContentAsString().isEmpty());
    }


    /*
    todo
    @Test
    @DisplayName("--- pendiente")
    @WithMockUser(username = "david", roles = {"LIFEGUARD"})
    void sendEmailShiftResume() {
    }

    @Test
    @DisplayName("Get staff by dni and shift id")
    @WithMockUser(username = "david", roles = {"LIFEGUARD"})
    void findUserStaffByDni() throws Exception {
        // No tiene id antes de guardarse en bd, por lo que hardcodeo id 1 ya que se crea y borra la bd cada vez que se corre el test
        MvcResult findByDniUserResult = mockMvc.perform(MockMvcRequestBuilders.get(BASE_URL+"1"+"/staff/"+user.getDni())).andReturn(); //{idShift}/staff/{dniUser}
        String respAsString = findByDniUserResult.getResponse().getContentAsString();
        UserStaffReadDto userReadDto = objectMapper.readValue(respAsString, UserStaffReadDto.class);

        assertEquals(findByDniUserResult.getResponse().getStatus() , HttpServletResponse.SC_OK);
        assertEquals(userReadDto.getDni(), user.getDni());
        assertEquals(userReadDto.getUsername(), user.getUsername());
        assertEquals(userReadDto.getRole(), user.getRole());
        assertEquals(userReadDto.getEmail(), user.getEmail());
    }
     */
    private String mapperStringToJSON(Object object) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(object);
    }
}