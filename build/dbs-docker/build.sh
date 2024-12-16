#!/bin/bash

# start the postgre container if exist
CONTAINER_NAME="postgre"
CONTAINER_ID=$(docker ps -aq -f status=exited -f name=$CONTAINER_NAME)

if [[ "$(docker ps -q -f name=$CONTAINER_NAME)" ]]; then
    echo "postgre already running on http://localhost:5433..."
else
    if [[ -n "$CONTAINER_ID" ]]; then
        echo "Starting the existing container..."
        docker start "$CONTAINER_ID"
    else
        echo "something wrong"
    fi
fi



