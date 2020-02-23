# Start from the latest golang base image
FROM golang:latest AS builder

WORKDIR /app
# Set an env var 
ENV SRC_DIR=/go/src
# Add the source code:
ADD . $SRC_DIR
# Build it:
RUN cd $SRC_DIR; ls -ltr
RUN go get github.com/spf13/viper
RUN cd $SRC_DIR; go build -o bot ./bot; 

FROM alpine:latest
WORKDIR /app
COPY --from=builder /go/src/bot/bot dest
RUN cp -r $SRC_DIR/config/ /app

ENTRYPOINT ["./bot"]
