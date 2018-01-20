package org.bal.app.controller;


import org.bal.app.grpc.client.PersonClient;
import org.bal.app.proto.internal.FileContent;
import org.bal.app.proto.internal.Person;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;


@RestController
public class PersonController {

    private static final Logger LOG = LoggerFactory.getLogger(PersonController.class);
    @Autowired
    private PersonClient personService;


    @RequestMapping("/hello")
    public String hello() {
        LocalDateTime currentDate = LocalDateTime.now();
        return "Hello World! -[" + currentDate + "]";
    }

    @RequestMapping("/person/{$id}")
    public String person(@RequestParam(value = "id", defaultValue = "123") String id) {
        Person person;
        person = personService.getPersonById(Integer.valueOf(id));
        return "Hello: " + person.getFirstName();
    }

    @RequestMapping("/randomNames")
    public String randomNames() {
        Person person;
        person = personService.randomNames();
        return "Hello " + person.getDescription() + " " + person.getFirstName();
    }

    @RequestMapping("whatsInTheFile")
    public String whatsInTheFile() {
        FileContent fileContent = personService.whatsInTheFile();
        LOG.info(fileContent.getContent());
        return fileContent.getContent();
    }

    @RequestMapping("/rightNow")
    public String rightNow() {
        LocalDateTime currentDate = LocalDateTime.now();
        return "Now is [" + currentDate + "]";
    }

}
