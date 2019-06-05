package org.bal.quote.server;


import com.google.protobuf.Empty;
import io.grpc.ManagedChannel;
import io.grpc.Server;
import io.grpc.inprocess.InProcessChannelBuilder;
import io.grpc.inprocess.InProcessServerBuilder;
import io.grpc.testing.GrpcCleanupRule;
import org.bal.quote.proto.internal.Quote;
import org.bal.quote.proto.internal.QuoteById;
import org.bal.quote.proto.internal.QuoteList;
import org.bal.quote.proto.internal.QuoteManagementGrpc;
import org.bal.quote.server.service.QuoteManagementService;
import org.bal.quote.server.config.Configuration;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = Configuration.class)
public class QuoteServerTest {
    /**
     * This creates and starts an in-process server, and creates a starters with an in-process channel.
     * When the app is done, it also shuts down the in-process starters and server.
     */
    @Rule
    public final GrpcCleanupRule grpcCleanup = new GrpcCleanupRule();

    @Autowired
    private QuoteManagementService service;

    private ManagedChannel inProcessChannel;
    private Server server;

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
    }

    /**
     * To app the server, make calls with a real stub using the in-process channel, and verify
     * behaviors or state changes from the starters side.
     */
    @Test
    public void should_find_the_quote_by_id() throws Exception {


        QuoteManagementGrpc.QuoteManagementBlockingStub blockingStub =
                QuoteManagementGrpc.newBlockingStub(inProcessChannel);


        Quote quote = blockingStub.getQuoteById(QuoteById.newBuilder().setId(0).build());

        assertThat(quote.getName()).isEqualTo("Steve Rogers");
    }

    /**
     * To app the server, make calls with a real stub using the in-process channel, and verify
     * behaviors or state changes from the starters side.
     */
    @Test
    public void should_return_all_quotes() throws Exception {

        QuoteManagementGrpc.QuoteManagementBlockingStub blockingStub =
                QuoteManagementGrpc.newBlockingStub(inProcessChannel);


        QuoteList quotes = blockingStub.allQuotes(Empty.newBuilder().build());

        assertThat(quotes.getQuotesList().size()).isGreaterThan(0);
    }

}