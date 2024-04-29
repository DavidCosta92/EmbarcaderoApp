package com.Embarcadero.demo.controllers;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

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

    @BeforeAll
    private void initialSetUp(){
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

    }

    @Test
    void addNewRecord() {
        // tengo que tener un shift creado


        // tengo registros simples
        // tengo registros con licencias

        /*
         Integer idShift;
         Date startTime;
         RecordState recordState;
         PersonAddDto person;
         Integer numberOfGuests;
         String car;
         String notes;

        // TODO SOLO ACCEPTABLE QUE VENGA UNO, no pueden venir o faltar ambos..
         LicenseReadDto license; // Es un read porque solo envio license code
         SimpleBoatAddDto simpleBoat; // es un add porque simpre creare un bote nuevo, ya que es un bote por cada registro!
         Boolean hasLicense;
        */
    }
    @Test
    void findAllRecords() {
    }


    @Test
    void updateRecord() {
    }

    @Test
    void findById() {
    }
}