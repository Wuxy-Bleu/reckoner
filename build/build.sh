#!/bin/bash

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
    mvn install
    cd - &> /dev/null
  done

  tmp=$(find ../ -type d -name "starters")
  cd "$tmp"/utils
  mvn install
  cd - &> /dev/null
  cd "$tmp"/constant
  mvn install
  cd - &> /dev/null
  cd "$tmp"/locale
  mvn install
  cd - &> /dev/null

  for dir in $( find ../ -type d -name "*-feign" ); do
    cd "$dir"
    mvn install
    cd -
  done
}

cd "$(dirname "$0")"
echo "Please select a choice:"
echo "[1] for intellij idea only, delete all iml and .idea folder"
echo "[2] mvn clean all"
echo "[3] mvn install infra"

while true; do
  read -rp "Enter your choice (1-3): " choice

  case $choice in
    [1-3])  # Check if choice is between 1 and 2
      if [ "$choice" -eq 1 ]; then
        func1
      elif  [ "$choice" -eq 2 ]; then
        func2
      else
        func3
      fi
      break  # Exit the loop if valid choice is entered
      ;;
    *)      # Handle invalid input
      echo "Invalid choice. Please enter 1 - 3."
      ;;
  esac
done