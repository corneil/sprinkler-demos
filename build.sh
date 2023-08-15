#!/usr/bin/env bash
./mvnw -s .settings.xml install verify -T 1C
./mvnw -s .settings.xml -pl :sprinkler-simulation-app,:sprinkler-timer-source,:sprinkler-decision-processor,:sprinkler-data-sink,:sprinkler-report spring-boot:build-image -T 1C
docker tag corneil/sprinkler-simulation-app:1.0.0-SNAPSHOT corneil/sprinkler-simulation-app:latest
docker tag corneil/sprinkler-timer-source:1.0.0-SNAPSHOT corneil/sprinkler-timer-source:latest
docker tag corneil/sprinkler-decision-processor:1.0.0-SNAPSHOT corneil/sprinkler-decision-processor:latest
docker tag corneil/sprinkler-data-sink:1.0.0-SNAPSHOT corneil/sprinkler-data-sink:latest
docker tag corneil/sprinkler-report:1.0.0 corneil/sprinkler-report:latest
