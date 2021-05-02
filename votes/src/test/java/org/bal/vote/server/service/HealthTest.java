package org.bal.vote.server.service;

import io.grpc.ManagedChannel;
import io.grpc.Server;
import io.grpc.health.v1.HealthCheckRequest;
import io.grpc.health.v1.HealthGrpc;
import io.grpc.inprocess.InProcessChannelBuilder;
import io.grpc.inprocess.InProcessServerBuilder;
import io.grpc.testing.GrpcCleanupRule;
import nl.altindag.log.LogCaptor;
import org.bal.vote.config.TestConfig;
import org.bal.vote.server.repository.VoteRepository;
import org.bal.vote.server.service.HealthService;
import org.junit.Rule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = TestConfig.class)
public class HealthTest {

    /**
     * This creates and starts an in-process server, and creates a starters with an in-process channel.
     * When the app is done, it also shuts down the in-process starters and server.
     */
    @Rule
    public final GrpcCleanupRule grpcCleanup = new GrpcCleanupRule();

    @MockBean
    private VoteRepository voteRepository;


    @Autowired
    private HealthService service;

    private ManagedChannel inProcessChannel;
    private Server server;

    protected InProcessChannelBuilder onChannelBuild(InProcessChannelBuilder channelBuilder){
        return  channelBuilder;
    }

    @BeforeEach
    public void setUp() throws Exception {
        // Generate a unique in-process server name.
        String serverName = InProcessServerBuilder.generateName();

        server = grpcCleanup.register(InProcessServerBuilder
                .forName(serverName).directExecutor().addService(service).build().start());

        inProcessChannel = grpcCleanup.register(InProcessChannelBuilder
                .forName(serverName).directExecutor().build());
    }

    @Test
    public void shouldReturnGoodHealth() throws Exception {

        HealthGrpc.HealthBlockingStub blockingStub = HealthGrpc.newBlockingStub(inProcessChannel);

        when(voteRepository.getVote(0)).thenReturn(Integer.valueOf(1));

        blockingStub.check(HealthCheckRequest.newBuilder().build());

        LogCaptor logCaptor = LogCaptor.forClass(HealthService.class);
        logCaptor.getDebugLogs().contains("OK");
    }


    @Test
    public void shouldReturnBadHealth() throws Exception {

        HealthGrpc.HealthBlockingStub blockingStub = HealthGrpc.newBlockingStub(inProcessChannel);

        when(voteRepository.getVote(0)).thenReturn(null);

        blockingStub.check(HealthCheckRequest.newBuilder().build());

        LogCaptor logCaptor = LogCaptor.forClass(HealthService.class);
        logCaptor.getDebugLogs().contains("NOT OK");
    }
}
