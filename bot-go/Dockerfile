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

<<<<<<< HEAD
FROM alpine:latest
WORKDIR /app
COPY --from=builder /go/src/bot/bot /app
COPY --from=builder /go/src/config /app

=======
>>>>>>> 3cdf91a25aad5646149764a66060e52bf8757373
ENTRYPOINT ["./bot"]
