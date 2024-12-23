#!/bin/bash

# Get the absolute path of the directory where this script is located
SCRIPT_DIR=$(cd "$(dirname "$0")" && pwd)

dbs_SCRIPT="$SCRIPT_DIR/dbs-docker/build.sh"
redis_SCRIPT="$SCRIPT_DIR/redis/build.sh"
rabbitmq_SCRIPT="$SCRIPT_DIR/mq-docker/build.sh"

function func1() {
  find ../../ -type f -name "*iml*" -delete
  find ../../ -type d -name "*.idea*" -delete
}

function func2() {
  for dir in $(find ../ -type f -name "pom.xml" | rev | cut -c 9- | rev); do
      cd "$dir"
      pwd | cat
      mvn clean -q
      cd - &> /dev/null
  done
}

function func3() {
  for dir in $(find ../ -type d -name "parent"); do
    cd "$dir"
    mvn clean install
    cd - &> /dev/null
  done

  tmp=$(find ../ -type d -name "starters")
  cd "$tmp"/utils
  mvn clean install
  cd - &> /dev/null
  cd "$tmp"/constant
  mvn clean install
  cd - &> /dev/null
  cd "$tmp"/locale
  mvn clean install
  cd - &> /dev/null

  for dir in $( find ../ -type d -name "*-feign" ); do
    cd "$dir"
    mvn clean install
    cd -
  done
}

function start-db-docker() {
    bash "$dbs_SCRIPT"
}

function start-redis-docker() {
    bash "$redis_SCRIPT"
}

function start-rabbitmq-docker() {
    bash "$rabbitmq_SCRIPT"
}

cd "$(dirname "$0")"
echo "Please select a choice:"
echo "[1] for intellij idea only, delete all iml and .idea folder"
echo "[2] mvn clean all"
echo "[3] mvn install infra"
echo "[4] start dbs on docker"
echo "[5] start redis stack on docker"
echo "[6] start rabbitmq on docker"

while true; do
  read -rp "Enter your choice (1-6): " choice

  case $choice in
    [1-6])  # Check if choice is between 1 and 2
      if [ "$choice" -eq 1 ]; then
        func1
      elif  [ "$choice" -eq 2 ]; then
        func2
      elif  [ "$choice" -eq 3 ]; then
        func3
      elif  [ "$choice" -eq 5 ]; then
        start-redis-docker
      elif  [ "$choice" -eq 6 ]; then
        start-rabbitmq-docker
      else
        start-db-docker
      fi
      break  # Exit the loop if valid choice is entered
      ;;
    *)      # Handle invalid input
      echo "Invalid choice. Please enter 1 - 6."
      ;;
  esac
done