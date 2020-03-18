# Build using `ko`

For linux download the latest `ko` from [here](https://github.com/google/ko/releases)

Or you can build `ko` by executing this.

### On linux
`GO111MODULE=on go get github.com/google/ko/cmd/ko`

### On windows

```shell
set GO111MODULE=on
go get github.com/google/ko/cmd/ko
```

## Publishing the binary to your registry

Follow the example below

```shell
$ export IMAGE_TAG=1.0.0
$ export KO_DOCKER_REPO=localhost:32000

$ cd <application path>/bot-go

$ ko publish -B -t $IMAGE_TAG --insecure-registry=true ./bot
```

The resulting image will look like this 

`localhost:32000/github.com/balchua/demo/bot-go/bot:1.0.0@sha256:4a0c7a40c3521f8dad19e1d7579139ba4e27a46e8a3be1d964eebe1ef14fc7de`

### Static files

When running in cluster, the static config files will be located in `/var/run/ko`.

The application will always use the environment variable `KO_DATA_PATH` to search for the configuration files.

To run locally,

```shell

$ KO_DATA_PATH=$PWD/bot/kodata go run ./bot/bot-runner.go
```

## Build with skaffold

Skaffold will execute `ko` as part of the custom build.

First you need to have the following environment variables present.
* `KO_DOCKER_REPO`
* `IMAGE_TAG`

To build using skaffold, simply execute `skaffold build-p local`

