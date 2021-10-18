package com.codium.bank.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;


@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties(
        value = {"createdDate", "lastUpdatedDate", "createdUser", "lastUpdatedUser"},
        allowGetters = true
)
public class BankAuditingEntity<U> implements Serializable {

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "createdDate", nullable = false, updatable = false)
    @CreatedDate
    private Date createdDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "lastUpdatedDate", nullable = false)
    @LastModifiedDate
    private Date lastUpdatedDate;

    @Column(name = "createdUser", nullable = false, updatable = false)
    @CreatedBy
    private U createdUser;

    @Column(name = "lastUpdatedUser", nullable = false)
    @LastModifiedBy
    private U lastUpdatedUser;

    public BankAuditingEntity() {
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getLastUpdatedDate() {
        return lastUpdatedDate;
    }

    public void setLastUpdatedDate(Date lastUpdatedDate) {
        this.lastUpdatedDate = lastUpdatedDate;
    }

    public U getCreatedUser() {
        return createdUser;
    }

    public void setCreatedUser(U createdUser) {
        this.createdUser = createdUser;
    }

    public U getLastUpdatedUser() {
        return lastUpdatedUser;
    }

    public void setLastUpdatedUser(U lastUpdatedUser) {
        this.lastUpdatedUser = lastUpdatedUser;
    }
}
