# Go parameters
    GOCMD=go
    GOBUILD=$(GOCMD) build
    GOCLEAN=$(GOCMD) clean
    GOTEST=$(GOCMD) test
    GOGET=$(GOCMD) get
    BINARY_NAME=bot
    BINARY_UNIX=$(BINARY_NAME)_unix
    
    all: clean test build
    build: 
	        $(GOBUILD) $(BINARY_NAME)
			docker build .
    test:
	        $(GOTEST) -v ./...
    clean:
	        $(GOCLEAN)
			rm -f $(BINARY_NAME)
			rm -f $(BINARY_UNIX)
    run:
	        $(GOBUILD) -o $(BINARY_NAME) -v ./...
			./$(BINARY_NAME)
    deps:
	        $(GOGET) github.com/kelseyhightower/envconfig
			$(GOGET) gopkg.in/yaml.v2
