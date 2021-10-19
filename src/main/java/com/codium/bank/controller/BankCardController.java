package com.codium.bank.controller;

import com.codium.bank.exception.EntityNotFoundException;
import com.codium.bank.model.BankCard;
import com.codium.bank.repository.BankCardRepository;
import com.codium.bank.repository.PersonRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/people/{personId}/cards")
public class BankCardController {

    private static final Logger logger = LoggerFactory.getLogger(BankCardController.class);
    private final PersonRepository personRepository;
    private final BankCardRepository bankCardRepository;

    @Autowired
    public BankCardController(PersonRepository personRepository, BankCardRepository bankCardRepository) {
        this.personRepository = personRepository;
        this.bankCardRepository = bankCardRepository;
    }

    @PostMapping("/save")
    public ResponseEntity<BankCard> createCard(@PathVariable(value = "personId") Long personId,
                                               @Valid @RequestBody BankCard bankCard) {
        return personRepository.findById(personId).map(person -> {
            bankCard.setPerson(person);
            BankCard savedCard = bankCardRepository.save(bankCard);
            URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{cardId}")
                    .buildAndExpand(savedCard.getCardId()).toUri();
            logger.info("Created person with ID {}.", savedCard.getCardId());

            return ResponseEntity.created(location).body(savedCard);
        }).orElseThrow(() -> new EntityNotFoundException(String.format("PersonId %s not found", personId)));
    }

    @DeleteMapping("/delete/{cardId}")
    public ResponseEntity<?> deleteCard(@PathVariable(value = "personId") Long personId,
                                        @PathVariable(value = "cardId") Long cardId) {
        return bankCardRepository.findByCardIdAndPersonId(cardId, personId).map(card -> {
            bankCardRepository.delete(card);
            logger.info("Deleted card with ID {}.", cardId);
            return ResponseEntity.ok().build();
        }).orElseThrow(() -> new EntityNotFoundException(
                String.format("Card not found with id %s and person id %s ", cardId, personId)));
    }

    @GetMapping("/list")
    @ResponseStatus(HttpStatus.OK)
    public List<BankCard> getAllCardsByPersonId(@PathVariable(value = "personId") Long personId) {
        return bankCardRepository.findByPersonId(personId);
    }

    @Cacheable(value = "bankCards", key = "#cardId")
    @GetMapping("/{cardId}")
    @ResponseStatus(HttpStatus.FOUND)
    public BankCard getById(@PathVariable(value = "personId") Long personId,
                            @PathVariable(value = "cardId") Long cardId) {
        return bankCardRepository.findByCardIdAndPersonId(cardId, personId).orElseThrow(() -> new EntityNotFoundException(
                String.format("Card not found with id %s ", cardId)));
    }
}
