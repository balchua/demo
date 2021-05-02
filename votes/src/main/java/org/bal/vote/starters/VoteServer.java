/*
 * Copyright 2015, gRPC Authors All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.bal.vote.starters;


import lombok.extern.slf4j.Slf4j;
import org.bal.vote.config.Configuration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Server that manages startup/shutdown of a {@code Greeter} server.
 */
@SpringBootApplication(scanBasePackageClasses = {Configuration.class}, scanBasePackages = "org.bal.vote.server")
@Slf4j
public class VoteServer {

    /**
     * Main launches the server from the command line.
     */
    public static void main(String... args) {

        SpringApplication.run(VoteServer.class);
    }


}