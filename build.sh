#!/usr/bin/env bash
MT="-T 0.5C"
./mvnw -B -s .settings.xml install verify $MT
docker tag corneil/sprinkler-simulation-app:1.0.0-SNAPSHOT ghcr.io/corneil/sprinkler-demos/sprinkler-simulation-app:latest
docker tag corneil/sprinkler-event-source:1.0.0-SNAPSHOT ghcr.io/corneil/sprinkler-demos/sprinkler-event-source:latest
docker tag corneil/sprinkler-decision-processor:1.0.0-SNAPSHOT ghcr.io/corneil/sprinkler-demos/sprinkler-decision-processor:latest
docker tag corneil/sprinkler-data-sink:1.0.0-SNAPSHOT ghcr.io/corneil/sprinkler-demos/sprinkler-data-sink:latest
docker tag corneil/sprinkler-report:1.0.0 ghcr.io/corneil/sprinkler-demos/sprinkler-report:latest
