package com.codium.bank.model;

import java.io.Serializable;
import java.util.List;

public interface PersonWithCards extends Serializable {
    Person getPerson();

    List<BankCard> getCards();
}
