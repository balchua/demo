FROM adoptopenjdk/openjdk11:jdk-11.0.3_7-debian-slim

RUN mkdir /tools/  \
    && curl -L https://github.com/grpc-ecosystem/grpc-health-probe/releases/download/v0.3.0/grpc_health_probe-linux-amd64 -o /tools/grpc_health_probe \
    && chmod +x /tools/grpc_health_probe