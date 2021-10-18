package com.codium.bank.controller;


import com.codium.bank.model.BankCard;
import com.codium.bank.model.Person;
import com.codium.bank.repository.BankCardRepository;
import com.codium.bank.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Date;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class ControllerTests {

    @Autowired
    protected MockMvc mockMvc;


    @Autowired
    private BankCardController bankCardController;



    @MockBean
    protected PersonRepository mockPersonRepository;

    @MockBean
    protected BankCardRepository mockBankCardRepository;

    protected Person getPerson()
    {
        Person person = new Person("Fero", "Tester");
        person.setId(1);
        return person;
    }

    protected BankCard getBankCard()
    {
        return new BankCard(1l,"2222-2222-2222", new Date(44444), "123", getPerson());

    }

}
