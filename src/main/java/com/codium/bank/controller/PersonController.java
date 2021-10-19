package com.codium.bank.controller;

import com.codium.bank.exception.EntityNotFoundException;
import com.codium.bank.model.BankCard;
import com.codium.bank.model.Person;
import com.codium.bank.model.PersonWithCards;
import com.codium.bank.model.PersonWithCardsImpl;
import com.codium.bank.repository.BankCardRepository;
import com.codium.bank.repository.PersonRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/people")
public class PersonController {

    private static final Logger logger = LoggerFactory.getLogger(PersonController.class);
    private final PersonRepository personRepository;
    private final BankCardRepository bankCardRepository;

    @Autowired
    public PersonController(PersonRepository personRepository, BankCardRepository bankCardRepository) {
        this.personRepository = personRepository;
        this.bankCardRepository = bankCardRepository;
    }

    @PostMapping("/save")
    @ResponseStatus(HttpStatus.CREATED)
    public PersonWithCards createPerson(@Valid @RequestBody PersonWithCardsImpl personWithCards) {
        Person person = new Person(personWithCards.getPerson().getFirstName(), personWithCards.getPerson().getLastName());
        Person savedPerson = personRepository.save(person);
        logger.info("Created person with ID {}.", person.getId());
        personWithCards.getCards().forEach(card -> card.setPerson(savedPerson));
        List<BankCard> savedCards = bankCardRepository.saveAll(personWithCards.getCards());
        return new PersonWithCardsImpl(person, savedCards);
    }

    @CachePut(value = "person", key = "#personId")
    @PutMapping("/update/{personId}")
    @ResponseStatus(HttpStatus.OK)
    public Person updatePerson(@PathVariable long personId, @Valid @RequestBody Person personRequest) {
        return personRepository.findById(personId).map(person -> {
            person.setFirstName(personRequest.getFirstName());
            person.setLastName(personRequest.getLastName());
            return personRepository.save(person);
        }).orElseThrow(() -> new EntityNotFoundException(String.format("PersonId %s not found", personId)));
    }

    @DeleteMapping("/{personId}")
    public ResponseEntity<?> delete(@PathVariable long personId) {
        logger.info("Deleting person with ID {}.", personId);
        return personRepository.findById(personId).map(person -> {
            personRepository.delete(person);
            return ResponseEntity.ok().build();
        }).orElseThrow(() -> new EntityNotFoundException(String.format("PersonId %s not found", personId)));
    }

    @Cacheable(value = "person", key = "#personId")
    @GetMapping("/{personId}")
    @ResponseStatus(HttpStatus.FOUND)
    public PersonWithCards getById(@PathVariable long personId) {
        logger.info("Getting person with ID {}.", personId);
        return personRepository.findById(personId).map(person ->
                        (new PersonWithCardsImpl(person, bankCardRepository.findByPersonId(person.getId()))))
                .orElseThrow(() -> new EntityNotFoundException(String.format("PersonId %s not found", personId)));
    }

    @GetMapping("/search")
    public ResponseEntity<List<? extends PersonWithCards>> getByFirstnameAndLastName(@Param("lastname") String lastname, @Param("firstname") String firstname) {
        List<Person> result = personRepository.findByLastNameAndFirstName(lastname, firstname);
        if (result.isEmpty()) {
            throw new EntityNotFoundException(String.format("Person name %s %s not found", lastname, firstname));
        }
        return ResponseEntity.ok(result.stream().map(person ->
                (new PersonWithCardsImpl(person, bankCardRepository.findByPersonId(person.getId())))).collect(Collectors.toList()));
    }

    @GetMapping("/list")
    @ResponseStatus(HttpStatus.OK)
    public List<PersonWithCards> getAll() {
        return personRepository.findAll().stream()
                .map(person -> new PersonWithCardsImpl(person, bankCardRepository.findByPersonId(person.getId())))
                .collect(Collectors.toList());
    }
}
