# Demo Kit
[![FOSSA Status](https://app.fossa.io/api/projects/git%2Bgithub.com%2Fbalchua%2Fdemo.svg?type=shield)](https://app.fossa.io/projects/git%2Bgithub.com%2Fbalchua%2Fdemo?ref=badge_shield)


This project is a simple demonstration of microservices using Springboot integrated with Zipkin.
It is a simple project that lists quotes from Marvel movies and users can vote which one of them they like.

The application then tally the votes.

## Prerequisites:

1. Kubernetes cluster (ex. [microk8s](https://microk8s.io/) ) 
2. Docker
3. [Skaffold](https://skaffold.dev/)
4. [Helm](https://helm.sh/)
4. [Maven](https://maven.apache.org/)
5. [jib-maven-plugin](https://github.com/GoogleContainerTools/jib/tree/master/jib-maven-plugin)

## Project structure
  

Main components are:
* `frontend/` - Hosts the web pages as well as RESTful access to backend services.
* `quotes/` - This component retrieves the Marvel character quotes data stored PostgresSQL.
* `votes/` - This component keeps track of the votes on each quote.
* `bot/` - The bot will randomly vote quotes to simmulate traffic into the kit.
* `bot-go/` - Golang version of bot.
* `protos/` - Keeps all `proto` files in one location.  
* `db/` - Contains all infrastructure related components such as PostgreSQL, Redis and flyway (to perform schema migrate).
   * `db/postgres` - Postgres kubernetes manifests.
   * `db/redis` - Redis kubernetes manifests.
   * `db/schema-migrate/` - Flyway schema migration manifests.  Run as Kubernetes `Job`.

* `zipkin/` - Stashes all Zipkin kubernetes manifests.


## Frontend

This is a springboot application that has a simple GUI, which calls the quote service to retrieve all the quotes, calls `vote` service to cast and tally votes.
The frontend exposed these REST endpoints.
* `/api/quote/list`
* `/api/vote/castVote`
* `/api/vote/tally`

UI style is brought to you by [Material Kit](https://demos.creative-tim.com/material-kit/index.html)

#### Build
Go to the directory `frontend/`

To build the project, do a `mvn clean install jib:dockerBuild`
For simplicity, you can also use the provided `skaffold.yaml` and run `skaffold run -p local` or `skaffold dev -p local` if you want continuous build and deploy to you local cluster.


#### Test
You can use Postman to test the frontend's RESTFul endpoints.

## Quote

This is another springboot application that retrieves quotes stored in PostgreSQL.
It uses [gRPC java](https://grpc.io/docs/tutorials/basic/java/) for communication.

Stacks:

* Springboot - 2.1.5.RELEASE
* [Lognet grpc-springboot-starter](https://github.com/LogNet/grpc-spring-boot-starter)

#### Build
Go to directory `quotes`

To build the project, do a `mvn clean install jib:dockerBuild`

#### Run

In order to run the `quotes` service, you need to have the *Quotes DB* up and running.

Use skaffold:
1.  Start the quotes DB (postgres)

`skaffold run -p quotes-db-local`

2.  Start the quotes service

`skaffold run -p local`

Checkout the [`skaffold.yaml`](quotes/skaffold.yaml).



#### Test
You can use [grpcurl](https://github.com/fullstorydev/grpcurl) to test it from command line.

#### Using `grpcurl`

To get all quotes

`grpcurl -plaintext localhost:50052 org.bal.vote.proto.internal.QuoteManagement/AllQuotes`

To get quote by Id:

`grpcurl -plaintext -d '{"quoteId":"0"}' localhost:50052 org.bal.vote.proto.internal.QuoteManagement/GetQuoteById`

## Vote

This is another springboot application that allows users to cast their favorite Marvel quotes.
It also tallies all the votes.

It uses [gRPC java](https://grpc.io/docs/tutorials/basic/java/) for communication.

It stores its data in Redis.

Stacks:

* Springboot - 2.1.5.RELEASE
* [Lognet grpc-springboot-starter](https://github.com/LogNet/grpc-spring-boot-starter)
* Redis

#### Build
To build the project, do a `mvn clean install jib:dockerBuild`
For simplicity, you can also use the provided `skaffold.yaml` and run `skaffold run -p local` or `skaffold dev -p local` if you want continuous build and deploy to you local cluster.


#### Run

In order to run the `votes` service, you need to have the *Quotes DB* up and running.

Use skaffold:
1.  Start the Votes DB (Redis)

`skaffold run -p votes-db-local`

2.  Start the vote service

`skaffold run -p local`

Checkout the [`skaffold.yaml`](votes/skaffold.yaml).


#### Test
You can use [grpcurl](https://github.com/fullstorydev/grpcurl) to test it from command line.

#### Using `grpcurl`

To get the votes tally

`grpcurl -plaintext  localhost:50052 org.bal.vote.proto.internal.VoteManagement/GetAllVotes`

To cast a vote:

`grpcurl -plaintext -d '{"quoteId":"0"}' localhost:50052 org.bal.vote.proto.internal.VoteManagement/CastVote`

#### Enabling Grafana Loki and Promtail

`helm upgrade --install loki loki/loki --namespace monitoring`

`helm upgrade --install promtail loki/promtail --set "loki.serviceName=loki --namespace monitoring"`


## Zipkin

To start Zipkin

`skaffold run -p local`

To stop Zipkin

`skaffold delete -p local`

Checkout the [`skaffold.yaml`](zipkin/skaffold.yaml).

## bot-go
The build uses a Dockerfile. Hence, docker daemon setup is required. 



## License
[![FOSSA Status](https://app.fossa.io/api/projects/git%2Bgithub.com%2Fbalchua%2Fdemo.svg?type=large)](https://app.fossa.io/projects/git%2Bgithub.com%2Fbalchua%2Fdemo?ref=badge_large)
