package org.bal.vote.server.service.client;


import io.grpc.ManagedChannel;
import io.grpc.Server;
import io.grpc.inprocess.InProcessChannelBuilder;
import io.grpc.inprocess.InProcessServerBuilder;
import io.grpc.testing.GrpcCleanupRule;
import lombok.extern.slf4j.Slf4j;
import org.bal.quote.proto.internal.Quote;
import org.bal.quote.proto.internal.QuoteManagementGrpc;
import org.bal.vote.config.TestConfig;
import org.junit.Rule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = TestConfig.class)
@Slf4j
public class QuoteServerClientTest {
    /**
     * This creates and starts an in-process server, and creates a starters with an in-process channel.
     * When the app is done, it also shuts down the in-process starters and server.
     */
    @Rule
    public final GrpcCleanupRule grpcCleanup = new GrpcCleanupRule();

    @Autowired
    private QuoteClient client;

    @Autowired
    private InProcessServerBuilder quoteChannelBuilder;

    private ManagedChannel inProcessChannel;
    private Server server;
    private static boolean isRegistered = false;

    protected InProcessChannelBuilder onChannelBuild(InProcessChannelBuilder channelBuilder) {
        return channelBuilder;
    }

    @BeforeEach
    public void setUp() throws Exception {
        // Generate a unique in-process server name.
        String serverName = InProcessServerBuilder.generateName();

        if (!QuoteServerClientTest.isRegistered) {
            server = grpcCleanup.register(quoteChannelBuilder.directExecutor().build().start());
            QuoteServerClientTest.isRegistered = true;
        }

        inProcessChannel = grpcCleanup.register(InProcessChannelBuilder
                .forName(serverName).directExecutor().build());
    }

    @Test
    public void shouldFindQuoteById() throws Exception {
        QuoteManagementGrpc.QuoteManagementBlockingStub blockingStub =
                QuoteManagementGrpc.newBlockingStub(inProcessChannel);
        Quote quote = client.getQuoteById(0);
        log.debug(quote.getName());
        assertThat(quote).isNotNull();

    }

    @Test
    public void shouldGetAllQuotes() throws Exception {
        QuoteManagementGrpc.QuoteManagementBlockingStub blockingStub =
                QuoteManagementGrpc.newBlockingStub(inProcessChannel);
        List<Quote> quotes = client.allQuotes();

        assertThat(quotes).hasSizeGreaterThan(0);

    }
}