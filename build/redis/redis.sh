#!/bin/bash

docker run -d -v "$(pwd)/redis.conf:/redis-stack.conf" --name redis-stack-t -p 5377:6379 -p 8011:8001 redis/redis-stack:latest