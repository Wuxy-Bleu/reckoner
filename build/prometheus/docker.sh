#!/bin/bash

PROMETHEUS_CONTAINER="prometheus"
GRAFANA_CONTAINER="grafana"

if docker inspect -f '{{.State.Running}}' "$PROMETHEUS_CONTAINER" 2>/dev/null; then
    echo "Stopping the PROMETHEUS_CONTAINER..."
    docker stop "$PROMETHEUS_CONTAINER"
    echo "Starting the PROMETHEUS_CONTAINER..."
    docker start "$PROMETHEUS_CONTAINER"
else
    echo "Starting the PROMETHEUS_CONTAINER..."
    docker run \
        -p 9090:9090 \
        -v "$(pwd)/prometheus.yml:/etc/prometheus/prometheus.yml" \
        --name prometheus \
        --hostname prometheus \
        prom/prometheus:v2.48.0
fi

if docker inspect -f '{{.State.Running}}' "$GRAFANA_CONTAINER" 2>/dev/null; then
    echo "Stopping the GRAFANA_CONTAINER..."
    docker stop "$GRAFANA_CONTAINER"
    echo "Starting the GRAFANA_CONTAINER..."
    docker start "$GRAFANA_CONTAINER"
else
    echo "Starting the GRAFANA_CONTAINER..."
docker run \
  -p 3000:3000 \
  --name grafana \
  --hostname grafana \
  grafana/grafana-enterprise:10.3.1
fi