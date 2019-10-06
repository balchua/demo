# Start from the latest golang base image
FROM golang:latest

WORKDIR /app
# Set an env var 
ENV SRC_DIR=/go/src
# Add the source code:
ADD ./src/ $SRC_DIR
# Build it:
RUN cd $SRC_DIR; ls -ltr
RUN go get github.com/kelseyhightower/envconfig
RUN go get gopkg.in/yaml.v2
RUN cd $SRC_DIR; go build -o bot ./bot; 
RUN cp $SRC_DIR/bot/bot /app
RUN cp -r $SRC_DIR/config/ /app

ENTRYPOINT ["./bot"]