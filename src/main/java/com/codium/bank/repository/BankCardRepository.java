package com.codium.bank.repository;

import com.codium.bank.model.BankCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BankCardRepository extends JpaRepository<BankCard, Long> {
    List<BankCard> findByPersonId(long personId);

    Optional<BankCard> findByCardIdAndPersonId(long cardId, long personId);
}