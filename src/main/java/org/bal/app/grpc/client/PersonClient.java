package org.bal.app.grpc.client;


import org.bal.app.proto.internal.Person;
import org.bal.app.proto.internal.PersonById;
import org.bal.app.proto.internal.PersonManagementGrpc.PersonManagementBlockingStub;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;


@Component
public class PersonClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(PersonClient.class);

    @Autowired
    @Qualifier("personManagementBlockingStub")
    private PersonManagementBlockingStub blockingStub;


    /**
     * @param personId, the person id
     */
    public Person getPersonById(int personId) {
        Person person = blockingStub.getPersonById(PersonById.newBuilder().setId(personId).build());

        LOGGER.debug("NAME: " + person.getFirstName());
        return person;

    }

    /*@Scheduled
    public void repeatForever() {
        LOGGER.debug("repeating?");
        getPersonById(123);
    }*/


}
