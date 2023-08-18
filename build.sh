#!/usr/bin/env bash
MT="-T 0.5C"
./mvnw -B -s .settings.xml install verify $MT
./mvnw -B -s .settings.xml -pl :sprinkler-simulation-app,:sprinkler-event-source,:sprinkler-decision-processor,:sprinkler-data-sink,:sprinkler-report spring-boot:build-image -DskipTests $MT
docker tag corneil/sprinkler-simulation-app:1.0.0-SNAPSHOT corneil/sprinkler-simulation-app:latest
docker tag corneil/sprinkler-event-source:1.0.0-SNAPSHOT corneil/sprinkler-event-source:latest
docker tag corneil/sprinkler-decision-processor:1.0.0-SNAPSHOT corneil/sprinkler-decision-processor:latest
docker tag corneil/sprinkler-data-sink:1.0.0-SNAPSHOT corneil/sprinkler-data-sink:latest
docker tag corneil/sprinkler-report:1.0.0 corneil/sprinkler-report:latest
