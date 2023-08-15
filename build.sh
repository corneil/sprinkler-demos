#!/usr/bin/env bash
./mvnw -s .settings.xml install verify -T 1C
./mvnw -s .settings.xml -pl :sprinkler-simulation-app,:sprinkler-timer-source,:sprinkler-decision-processor,:sprinkler-data-sink spring-boot:build-image -T 1C
