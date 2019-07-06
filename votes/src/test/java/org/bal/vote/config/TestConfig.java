package org.bal.vote.config;


import com.google.protobuf.Empty;
import io.grpc.ManagedChannel;
import io.grpc.Server;
import io.grpc.inprocess.InProcessChannelBuilder;
import io.grpc.inprocess.InProcessServerBuilder;
import io.grpc.stub.StreamObserver;
import org.bal.quote.proto.internal.Quote;
import org.bal.quote.proto.internal.QuoteById;
import org.bal.quote.proto.internal.QuoteList;
import org.bal.quote.proto.internal.QuoteManagementGrpc;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@ComponentScan(basePackages = "org.bal.vote.server")

@Import({org.bal.vote.config.Configuration.class, RedisConfiguration.class})
public class TestConfig {

    @Bean
    public InProcessServerBuilder quoteServerBuilder() {
        return (InProcessServerBuilder
                .forName("quoteServer").directExecutor().addService(new QuoteManagementGrpc.QuoteManagementImplBase() {

                    @Override
                    public void getQuoteById(QuoteById request,
                                             StreamObserver<Quote> responseObserver) {


                        responseObserver.onNext(Quote.newBuilder().setId(0).setQuote("Test").build());
                        responseObserver.onCompleted();
                    }

                    @Override
                    public void allQuotes(Empty request, StreamObserver<QuoteList> responseObserver) {

                        Quote q = Quote.newBuilder().setId(0).setQuote("Test").build();
                        QuoteList.Builder builder = QuoteList.newBuilder();
                        builder.addQuotes(q);
                        responseObserver.onNext(builder.build());
                        responseObserver.onCompleted();
                    }


                }));
    }

    @Bean("quoteServer")
    public Server quoteServer() {
        return quoteServerBuilder().build();
    }


    public ManagedChannel channelBuilder() {
        return InProcessChannelBuilder.forName("quoteServer").directExecutor().build();
    }

    @Bean("quoteManagementBlockingStub")
    public QuoteManagementGrpc.QuoteManagementBlockingStub quoteManagementBlockingStub() {
        return QuoteManagementGrpc.newBlockingStub(channelBuilder());
    }
}
