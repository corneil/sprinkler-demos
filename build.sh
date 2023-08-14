#!/usr/bin/env bash
./mvnw install verify -T 1C
./mvnw -pl :sprinkler-simulation-app,:sprinkler-timer-source,:sprinkler-decision-processor,:sprinkler-data-sink spring-boot:build-image -T 1C

