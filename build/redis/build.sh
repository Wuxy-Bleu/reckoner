#!/bin/bash

#docker run -d -v "$(pwd)/redis.conf:/redis-stack.conf" --name redis-stack-t -p 5377:6379 -p 8011:8001 redis/redis-stack:latest
# start the postgre container if exist
CONTAINER_NAME="redis-stack-t"
CONTAINER_ID=$(docker ps -aq -f status=exited -f name=$CONTAINER_NAME)

if [[ "$(docker ps -q -f name=$CONTAINER_NAME)" ]]; then
    echo "${CONTAINER_NAME} already running on http://localhost:5377..."
else
    if [[ -n "$CONTAINER_ID" ]]; then
        echo "Starting the existing container..."
        docker start "$CONTAINER_ID"
    else
        echo "something wrong"
    fi
fi



