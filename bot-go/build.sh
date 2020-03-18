#!/usr/bin/env bash
set -e

if [ -z "$KO_DOCKER_REPO" ]
then
      echo "Please set \$KO_DOCKER_REPO."
      exit 1
fi

if [ -z "$IMAGE_TAG" ]
then
      echo "\$IMAGE_TAG is empty, setting to latest."
      IMAGE_TAG="latest"
fi

ko publish -B -t $IMAGE_TAG --insecure-registry=true ./bot-go 
