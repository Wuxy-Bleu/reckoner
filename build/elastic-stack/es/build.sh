#!/bin/bash

#docker run -it -p 9222:9200 -p 9322:9300 --name esZ \
#-e cluster.routing.allocation.disk.watermark.high=95% -e cluster.routing.allocation.disk.watermark.low=90%  \
#-v /Users/lyux/Code/Proj/spring-experiment/reckoner-all/reckoner/build/elastic-stack/es/log4j2.properties:/usr/share/elasticsearch/config/log4j2.properties \
#--hostname=esZ   elasticsearch:8.13.0 \
#| tee -a /Users/lyux/Code/Proj/spring-experiment/reckoner-all/reckoner/build/elastic-stack/logs/es.log

#docker run -it -p 5592:9200 -p 5593:9300 --name esZ-node-1 \
#-e cluster.routing.allocation.disk.watermark.high=95% -e cluster.routing.allocation.disk.watermark.low=90%  \
#-e "cluster.name=docker-cluster" \
#-e "enrollment-token=eyJ2ZXIiOiI4LjEzLjAiLCJhZHIiOlsiMTcyLjE3LjAuODo5MjAwIl0sImZnciI6IjhiMzczMTdmMjMyODYwZjE5MjJiNjlmNTFiNzhkNGJjMWZmMjY5OTc0MzdjZWZiOTcyMzQyZDM4MTRlOTIyY2IiLCJrZXkiOiJhOTZoM1k0QlJjaE96X3pSY2k4QTpEbUN3UVgzaVJVeXJNZWZITVVONDFRIn0=" \
#-v /Users/lyux/Code/Proj/spring-experiment/reckoner-all/reckoner/build/elastic-stack/es/log4j2.properties:/usr/share/elasticsearch/config/log4j2.properties \
#--hostname=esZ-node-1   elasticsearch:8.13.0 \
#| tee -a /Users/lyux/Code/Proj/spring-experiment/reckoner-all/reckoner/build/elastic-stack/logs/es-node-1.log

#docker run --name kidZ --hostname kidZ  -p 5622:5601 \
#-v /Users/lyux/Code/Proj/spring-experiment/reckoner-all/reckoner/build/elastic-stack/es/kibana.yml:/usr/share/kibana/config/kibana.yml \
#docker.elastic.co/kibana/kibana:8.13.0 \
#| tee -a /Users/lyux/Code/Proj/spring-experiment/reckoner-all/reckoner/build/elastic-stack/logs/kibana.log

ES_DIR="/Users/lyux/Code/Proj/spring-experiment/reckoner-all/reckoner/build/elastic-stack/es"
LOG_DIR="/Users/lyux/Code/Proj/spring-experiment/reckoner-all/reckoner/build/elastic-stack/logs"
CFG_DIR="/Users/lyux/Code/Proj/spring-experiment/reckoner-all/reckoner/build/elastic-stack/mount-cfgs"

if docker ps -a --format '{{.Names}}' | grep -q "^es-master$"; then
    if docker ps --format '{{.Names}}' | grep -q "^es-master$"; then
        echo "Container 'es-master' is already running."
    else
        docker start es-master
    fi
else
    docker run -it -p 4900:9200 -p 4930:9300 --name es-master \
    -v "${ES_DIR}/log4j2.properties:/usr/share/elasticsearch/config/log4j2.properties" \
    -v "${LOG_DIR}/es-master:/tmp/es" \
    -e cluster.routing.allocation.disk.watermark.high=95% -e cluster.routing.allocation.disk.watermark.low=90%  \
    -e "cluster.name=Z-cluster" \
    -e "path.logs=/tmp/es" \
    --hostname=es-master   elasticsearch:8.13.0
fi

if docker ps -a --format '{{.Names}}' | grep -q "^es-node-0$"; then
    if docker ps --format '{{.Names}}' | grep -q "^es-node-0$"; then
        echo "Container 'es-node-0' is already running."
    else
        docker start es-node-0
    fi
else
    docker run -it -p 4911:9200 -p 4931:9300 \
    -m 8GB \
    --name es-node-0 \
    -v "${ES_DIR}/log4j2.properties:/usr/share/elasticsearch/config/log4j2.properties" \
    -v "${LOG_DIR}/es-node-0:/tmp/es" \
    -e "cluster.routing.allocation.disk.watermark.high=95%" \
    -e "cluster.routing.allocation.disk.watermark.low=90%"  \
    -e "cluster.name=Z-cluster" \
    -e "path.logs=/tmp/es" \
    -e "ES_JAVA_OPTS=-Xms512m -Xmx512m" \
    -e "discovery.seed_hosts=es-master:4930" \
    -e "node.roles=data,data_hot,data_content,data_warm,data_cold,data_frozen,ingest" \
    -e "xpack.security.enabled=true" \
    -e "xpack.security.enrollment.enabled=true" \
    -e "xpack.security.http.ssl.enabled=true" \
    -e "xpack.security.http.ssl.keystore.path=certs/http.p12" \
    -e "xpack.security.transport.ssl.enabled=true" \
    -e "xpack.security.transport.ssl.verification_mode=certificate" \
    -e "xpack.security.transport.ssl.keystore.path=certs/transport.p12" \
    -e "xpack.security.transport.ssl.truststore.path=certs/transport.p12" \
    -e "cluster.initial_master_nodes=es-master" \
    --hostname=es-node-0   \
    elasticsearch:8.13.0
fi