package org.bal.frontend.controller;


import io.grpc.health.v1.HealthCheckResponse;
import lombok.extern.slf4j.Slf4j;
import org.bal.frontend.grpc.client.QuoteClient;
import org.bal.frontend.grpc.client.VoteClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
@Slf4j
public class HealthController {

    @Autowired
    private QuoteClient quoteClient;

    @Autowired
    private VoteClient voteClient;


    @RequestMapping(value = "/ping", method = {RequestMethod.GET})
    public ResponseEntity ping() {
        HealthCheckResponse.ServingStatus quoteResponse = quoteClient.health();
        HealthCheckResponse.ServingStatus voteResponse = voteClient.health();

        if (quoteResponse == HealthCheckResponse.ServingStatus.SERVING && voteResponse == HealthCheckResponse.ServingStatus.SERVING) {
            return new ResponseEntity(HttpStatus.OK);
        } else {
            return new ResponseEntity(HttpStatus.BAD_GATEWAY);
        }
    }


}
