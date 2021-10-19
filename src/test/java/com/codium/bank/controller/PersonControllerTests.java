package com.codium.bank.controller;

import com.codium.bank.model.BankCard;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class PersonControllerTests extends ControllerTests {

    protected static final String NEW_PERSON = "{\n" +
            "    \"firstName\": \"Fero\",\n" +
            "    \"lastName\": \"Tester\",\n" +
            "    \"cards\": [\n" +
            "        {\n" +
            "            \"cardNumber\": \"1111-1111-1111-1111\",\n" +
            "            \"expirationDate\": \"2022-02-01\",\n" +
            "            \"cvvCode\": \"123\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"cardNumber\": \"2222-22222-22222-2222\",\n" +
            "            \"expirationDate\": \"2023-06-01\",\n" +
            "            \"cvvCode\": \"321\"\n" +
            "        }\n" +
            "    ]\n" +
            "}";


    @Autowired
    private PersonController controller;

    @Test
    public void contextLoads() {
        assertThat(controller).isNotNull();
    }

    @Test
    public void should_GetPeopleList_When_ValidRequest() throws Exception {
        BankCard card = getBankCard();
        given(this.mockPersonRepository.findAll()).willReturn(List.of(card.getPerson()));

        given(this.mockBankCardRepository.findByPersonId(1L)).willReturn(List.of(card));

        mockMvc.perform(get("/people/list")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].firstName").value(FIRST_NAME))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].cards").isArray());
    }

    @Test
    public void should_SearchPerson_When_ValidRequest() throws Exception {
        given(this.mockPersonRepository.findByLastNameAndFirstName(LAST_NAME, FIRST_NAME)).willReturn(List.of(getPerson()));
        mockMvc.perform(get("/people/search?firstname=Fero&lastname=Tester")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].firstName").value(FIRST_NAME))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].lastName").value(LAST_NAME));
    }

    @Test
    public void should_SearchPerson_When_InvalidRequest() throws Exception {
        given(this.mockPersonRepository.findByLastNameAndFirstName(LAST_NAME, FIRST_NAME)).willReturn(List.of(getPerson()));
        mockMvc.perform(get("/people/search?firstname=Fero&lastname=Tester77")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    //@Test
    public void should_GetPersonById_When_ValidRequest() throws Exception {
        given(this.mockPersonRepository.findById(anyLong())).willReturn(Optional.of(getPerson()));
        mockMvc.perform(get("/people/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value(FIRST_NAME));
    }

    @Test
    public void createPerson() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/people/save")
                        .content(NEW_PERSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").exists());
    }


}
