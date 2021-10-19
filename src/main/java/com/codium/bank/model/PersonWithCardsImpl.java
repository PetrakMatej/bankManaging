package com.codium.bank.model;

import com.fasterxml.jackson.annotation.JsonCreator;

import java.util.List;

public class PersonWithCardsImpl implements PersonWithCards {

    private Person person;
    private List<BankCard> cards;


    @Override
    public Person getPerson() {
        return person;
    }

    @Override
    public List<BankCard> getCards() {
        return cards;
    }

    @JsonCreator
    public PersonWithCardsImpl(Person person, List<BankCard> cards) {
        this.person = person;
        this.cards = cards;
    }

}
