package org.bal.quote.server;


import com.google.protobuf.Empty;
import io.grpc.ManagedChannel;
import io.grpc.Server;
import io.grpc.inprocess.InProcessChannelBuilder;
import io.grpc.inprocess.InProcessServerBuilder;
import io.grpc.testing.GrpcCleanupRule;
import org.bal.quote.config.Configuration;
import org.bal.quote.proto.internal.Quote;
import org.bal.quote.proto.internal.QuoteById;
import org.bal.quote.proto.internal.QuoteList;
import org.bal.quote.proto.internal.QuoteManagementGrpc;
import org.bal.quote.server.repository.QuoteEntity;
import org.bal.quote.server.repository.QuoteRepository;
import org.bal.quote.server.service.QuoteManagementService;
import org.junit.Rule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;


@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = Configuration.class)
public class QuoteServerTest {
    /**
     * This creates and starts an in-process server, and creates a starters with an in-process channel.
     * When the app is done, it also shuts down the in-process starters and server.
     */
    @Rule
    public final GrpcCleanupRule grpcCleanup = new GrpcCleanupRule();

    @MockBean
    private QuoteRepository quoteRepository;


    @Autowired
    private QuoteManagementService service;

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

    /**
     * To app the server, make calls with a real stub using the in-process channel, and verify
     * behaviors or state changes from the starters side.
     */
    @Test
    public void should_find_the_quote_by_id() throws Exception {

        QuoteEntity entity = new QuoteEntity();
        entity.setId(0);
        entity.setName("Steve Rogers");
        entity.setQuote("test me");


        QuoteManagementGrpc.QuoteManagementBlockingStub blockingStub =
                QuoteManagementGrpc.newBlockingStub(inProcessChannel);

        when(quoteRepository.findById(any(Integer.class))).thenReturn(Optional.of(entity));


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

        QuoteEntity entity = new QuoteEntity();
        entity.setId(0);
        entity.setName("Steve Rogers");
        entity.setQuote("im testing ok?");

        List<QuoteEntity> entities = new ArrayList<>();
        entities.add(entity);

        when(quoteRepository.findAll()).thenReturn(entities);
        QuoteList quotes = blockingStub.allQuotes(Empty.newBuilder().build());

        assertThat(quotes.getQuotesList().size()).isGreaterThan(0);
    }

}