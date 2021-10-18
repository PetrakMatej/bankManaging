package com.codium.bank.model;

import com.fasterxml.jackson.annotation.JsonCreator;

import java.util.List;

public class PersonWithCardsImpl implements PersonWithCards {

    private long id;
    private String firstName;
    private String lastName;
    private List<BankCard> cards;

    @Override
    public long getId() {
        return id;
    }

    @Override
    public String getFirstName() {
        return firstName;
    }

    @Override
    public String getLastName() {
        return lastName;
    }

    @Override
    public List<BankCard> getCards() {
        return cards;
    }

    public PersonWithCardsImpl(Person person, List<BankCard> cards) {
        this.id = person.getId();
        this.firstName = person.getFirstName();
        this.lastName = person.getLastName();
        this.cards = cards;
    }

    @JsonCreator
    public PersonWithCardsImpl(String firstName, String lastName, List<BankCard> cards) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.cards = cards;
    }
}
