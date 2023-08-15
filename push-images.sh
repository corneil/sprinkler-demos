#!/usr/bin/env bash
docker push corneil/sprinkler-simulation-app:latest
docker push corneil/sprinkler-event-source:latest
docker push corneil/sprinkler-decision-processor:latest
docker push corneil/sprinkler-data-sink:latest
docker push corneil/sprinkler-report:latest
