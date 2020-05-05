package com.baeldung.patterns.es;

import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.baeldung.patterns.domain.Address;
import com.baeldung.patterns.domain.Contact;
import com.baeldung.patterns.es.repository.EventStore;
import com.baeldung.patterns.es.service.UserService;

public class Main {

    public static void main(String[] args) throws Exception {

        EventStore repository = new EventStore();
        UserService service = new UserService(repository);
        String userId = UUID.randomUUID()
            .toString();

        service.createUser(userId, "Tom", "Sawyer");
        service.updateUser(userId, 
            Stream.of(
              new Contact("EMAIL", "tom.sawyer@gmail.com"), 
              new Contact("EMAIL", "tom.sawyer@rediff.com"), 
              new Contact("PHONE", "700-000-0001"))
            .collect(Collectors.toSet()),
            Stream.of(
              new Address("New York", "NY", "10001"), 
              new Address("Los Angeles", "CA", "90001"), 
              new Address("Housten", "TX", "77001"))
            .collect(Collectors.toSet()));
        service.updateUser(userId, 
            Stream.of(
              new Contact("EMAIL", "tom.sawyer@gmail.com"), 
              new Contact("PHONE", "700-000-0001"))
            .collect(Collectors.toSet()),
            Stream.of(
              new Address("New York", "NY", "10001"), 
              new Address("Housten", "TX", "77001"))
            .collect(Collectors.toSet()));

        System.out.println(service.getContactByType(userId, "EMAIL"));
        System.out.println(service.getAddressByRegion(userId, "NY"));

    }

}
