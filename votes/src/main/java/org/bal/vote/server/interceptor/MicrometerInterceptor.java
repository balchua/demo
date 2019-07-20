package org.bal.vote.server.interceptor;

import io.grpc.Metadata;
import io.grpc.ServerCall;
import io.grpc.ServerCallHandler;
import io.grpc.ServerInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class MicrometerInterceptor implements ServerInterceptor {

    @Autowired(required = true)
    @Qualifier("micrometerServerInterceptor")
    private ServerInterceptor micrometerInterceptor;


    @Override
    public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(ServerCall<ReqT, RespT> serverCall, Metadata metadata, ServerCallHandler<ReqT, RespT> serverCallHandler) {

        return micrometerInterceptor.interceptCall(serverCall, metadata, serverCallHandler);
    }
}
