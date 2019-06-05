package org.bal.quote.server.interceptor;

import io.grpc.Metadata;
import io.grpc.ServerCall;
import io.grpc.ServerCallHandler;
import io.grpc.ServerInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class ZipkinServerInterceptor implements ServerInterceptor {

    @Autowired(required = true)
    @Qualifier("grpcServerInterceptor")
    private ServerInterceptor grpcTracingInterceptor;


    @Override
    public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(ServerCall<ReqT, RespT> serverCall, Metadata metadata, ServerCallHandler<ReqT, RespT> serverCallHandler) {
        return grpcTracingInterceptor.interceptCall(serverCall, metadata, serverCallHandler);
    }
}
