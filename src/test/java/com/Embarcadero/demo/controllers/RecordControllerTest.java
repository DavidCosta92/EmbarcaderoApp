package com.Embarcadero.demo.controllers;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

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

    @Test
    void findAllRecords() {
    }

    @Test
    void addNewRecord() {
    }

    @Test
    void updateRecord() {
    }

    @Test
    void findById() {
    }
}