package com.vigulear.restdemo.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vigulear.restdemo.entity.Person;
import com.vigulear.restdemo.service.PersonService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

/**
 * @author Constantin Vigulear
 */
@SpringBootTest
class PersonControllerTest {

  @Mock PersonService personService;

  @BeforeEach
  void setUp() {}

  @Test
  void getPersonById() throws Exception {
    Person person = new Person().setId(1L);
    ObjectMapper objectMapper = new ObjectMapper();
    MockMvc mockMvc = MockMvcBuilders.standaloneSetup(new PersonController(personService)).build();

    when(personService.getPersonById(1L)).thenReturn(person);

    String responseContent =
        mockMvc
            .perform(
                MockMvcRequestBuilders.get("/people/{id}", person.getId())
                    .contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
            .andReturn()
            .getResponse()
            .getContentAsString();

    Person responsePerson = objectMapper.readValue(responseContent, Person.class);
    String responsePersonString = objectMapper.writeValueAsString(person);

    assertEquals(person, responsePerson);
    assertEquals(responsePersonString, responseContent);
  }
}
