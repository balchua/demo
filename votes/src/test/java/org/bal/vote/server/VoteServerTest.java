package org.bal.vote.server;

import com.google.protobuf.Empty;
import io.grpc.ManagedChannel;
import io.grpc.Server;
import io.grpc.inprocess.InProcessChannelBuilder;
import io.grpc.inprocess.InProcessServerBuilder;
import io.grpc.testing.GrpcCleanupRule;
import lombok.extern.slf4j.Slf4j;
import org.bal.vote.config.TestConfig;
import org.bal.vote.proto.internal.VoteManagementGrpc;
import org.bal.vote.proto.internal.VoteRequest;
import org.bal.vote.proto.internal.VotesList;
import org.bal.vote.server.service.VoteManagementService;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = {TestConfig.class})
@Slf4j
public class VoteServerTest {

    /**
     * This creates and starts an in-process server, and creates a starters with an in-process channel.
     * When the app is done, it also shuts down the in-process starters and server.
     */
    @Rule
    public final GrpcCleanupRule grpcCleanup = new GrpcCleanupRule();

    @Autowired
    private VoteManagementService service;

    private ManagedChannel inProcessChannel;
    private Server server;

    @Autowired
    @Qualifier("quoteServer")
    private Server quoteServer;

    protected InProcessChannelBuilder onChannelBuild(InProcessChannelBuilder channelBuilder){
        return  channelBuilder;
    }

    @Before
    public void setUp() throws Exception {
        // Generate a unique in-process server name.
        String serverName = InProcessServerBuilder.generateName();

        server = grpcCleanup.register(InProcessServerBuilder
                .forName(serverName).directExecutor().addService(service).build().start());

        inProcessChannel = grpcCleanup.register(InProcessChannelBuilder
                .forName(serverName).directExecutor().build());


        quoteServer.start();


    }

    /**
     * To app the server, make calls with a real stub using the in-process channel, and verify
     * behaviors or state changes from the starters side.
     */
    @Test
    public void should_add_a_vote() throws Exception {

        VoteManagementGrpc.VoteManagementBlockingStub blockingStub =
                VoteManagementGrpc.newBlockingStub(inProcessChannel);

        VoteRequest vote = VoteRequest.newBuilder().setQuoteId(0).build();
        blockingStub.castVote(vote);

        VotesList votes = blockingStub.getAllVotes(Empty.newBuilder().build());
        assertThat(votes.getVotesList().size()).isEqualTo(1);
        log.info("Current vote: " + votes.getVotes(0).getCount());

    }


}