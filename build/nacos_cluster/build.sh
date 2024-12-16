#!/bin/bash

CONTAINER_NAME="custom-nginx-container"
MYSQL_NAME="mysql-pri"
PORT=10101
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"

if [ "$1" == "new" ]; then
  if [ "$(docker ps -q -f name=$CONTAINER_NAME)" ]; then
    docker stop $CONTAINER_NAME
    echo "Stopped $CONTAINER_NAME container."
  fi
  if [ "$(docker ps -a -q -f name=$CONTAINER_NAME)" ]; then
    docker rm $CONTAINER_NAME
    echo "Deleted $CONTAINER_NAME container."
  else
    echo "$CONTAINER_NAME container does not exist. starting a new container...."
    docker run -d -p $PORT:80 --name $CONTAINER_NAME -v "$SCRIPT_DIR/nginx.conf":/etc/nginx/nginx.conf nginx
  fi
fi

if [[ "$(docker ps -q -f name=$CONTAINER_NAME)" ]]; then
    echo "nginx already running on http://localhost:$PORT..."
else
    if [[ "$(docker ps -aq -f status=exited -f name=$CONTAINER_NAME)" ]]; then
        echo "Starting the existing container..."
        docker start $CONTAINER_NAME
    else
        echo "something wrong"
    fi
fi

if [[ "$(docker ps -q -f name=$MYSQL_NAME)" ]]; then
    echo "mysql already running on  http://localhost:3306..."
else
    if [[ "$(docker ps -aq -f status=exited -f name=$MYSQL_NAME)" ]]; then
        echo "Starting the existing container..."
        docker start $MYSQL_NAME
    else
        echo "something wrong"
    fi
fi

