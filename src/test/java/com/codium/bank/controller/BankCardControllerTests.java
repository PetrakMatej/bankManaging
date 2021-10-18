package com.codium.bank.controller;

import com.codium.bank.model.BankCard;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
public class BankCardControllerTests extends ControllerTests {

    public static final String NEW_CARD = "{\n" +
            "    \"cardNumber\": \"333333333\",\n" +
            "    \"expirationDate\": \"2025-11-03\",\n" +
            "    \"cvvCode\": \"152\"\n" +
            "}";
    @Autowired
    private BankCardController controller;

    @Test
    public void contextLoads() throws Exception {
        assertThat(controller).isNotNull();
    }

    @Test
    public void should_GetPersonCardList_When_ValidRequest() throws Exception {
        BankCard card = getBankCard();
        given(this.mockBankCardRepository.findByPersonId(1l)).willReturn(List.of(card));

        mockMvc.perform(get("/people/1/cards/list")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].cardId").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].cardNumber").value("2222-2222-2222"));
    }

    @Test
    public void should_GetPersonCardById_When_ValidRequest() throws Exception {
        given(this.mockBankCardRepository.findByCardIdAndPersonId(1l, 1l)).willReturn(Optional.of(getBankCard()));
        mockMvc.perform(get("/people/1/cards/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.cardId").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.cardNumber").value("2222-2222-2222"));
    }

    @Test
    public void createCard() throws Exception {
        given(this.mockPersonRepository.findById(1l)).willReturn(Optional.of(getPerson()));
        given(this.mockBankCardRepository.save(any())).willReturn(getBankCard());
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/people/1/cards/save")
                        .content(NEW_CARD)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.cardId").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.cardId").value(1));
    }
}
