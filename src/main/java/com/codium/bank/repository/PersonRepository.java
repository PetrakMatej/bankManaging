package com.codium.bank.repository;

import com.codium.bank.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {

    @Query("select p from Person p where p.firstName = :firstname and p.lastName = :lastname")
    List<Person> findByLastNameAndFirstName(@Param("lastname") String lastname,
                                            @Param("firstname") String firstname);
}
