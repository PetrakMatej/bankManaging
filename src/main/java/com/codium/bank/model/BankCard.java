package com.codium.bank.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "bankcards")
public class BankCard extends BankAuditingEntity<String> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long cardId;

    private String cardNumber;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date expirationDate;

    private String cvvCode;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "person_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private Person person;

    public BankCard() {
    }

    public BankCard(long cardId, String cardNumber, Date expirationDate, String cvvCode, Person person) {
        this.cardId = cardId;
        this.cardNumber = cardNumber;
        this.expirationDate = expirationDate;
        this.cvvCode = cvvCode;
        this.person = person;
    }

    public long getCardId() {
        return cardId;
    }

    public void setCardId(long cardId) {
        this.cardId = cardId;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
    }

    public String getCvvCode() {
        return cvvCode;
    }

    public void setCvvCode(String cvvCode) {
        this.cvvCode = cvvCode;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }
}
