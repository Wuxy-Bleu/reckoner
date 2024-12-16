#!/bin/bash

# start the postgre container if exist
CONTAINER_NAME="rabbit"
CONTAINER_ID=$(docker ps -aq -f status=exited -f name=$CONTAINER_NAME)

if [[ "$(docker ps -q -f name=$CONTAINER_NAME)" ]]; then
    echo "rabbit already running on http://localhost:5672..."
else
    if [[ -n "$CONTAINER_ID" ]]; then
        echo "Starting the existing container..."
        docker start "$CONTAINER_ID"
    else
        docker run -d \
        --hostname rabbit \
        --name rabbit \
        -p 5672:5672 \
        -p 15672:15672 \
        -e RABBITMQ_DEFAULT_USER=user \
        -e RABBITMQ_DEFAULT_PASS=123 \
        rabbitmq:4.1-rc-management-alpine
    fi
fi



