package org.bal.app.controller;


import org.bal.app.grpc.client.PersonClient;
import org.bal.app.proto.internal.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
public class PersonController {


    @Autowired
    private PersonClient personService;

    @RequestMapping("/")
    public String home() {
        LocalDateTime currentDate = LocalDateTime.now();
        return "Hello World! -[" + currentDate + "]";
    }

    @RequestMapping("/person/{$id}")
    public String person(@RequestParam(value = "id", defaultValue = "123") String id) {
        Person person;
        person = personService.getPersonById(Integer.valueOf(id));
        return "Hello: " + person.getFirstName();
    }
}
