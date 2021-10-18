package com.codium.bank.model;

import java.io.Serializable;
import java.util.List;

public interface PersonWithCards extends Serializable {
    long getId();

    String getFirstName();

    String getLastName();

    List<BankCard> getCards();
}
