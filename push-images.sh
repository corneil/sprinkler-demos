#!/usr/bin/env bash
IMAGES="sprinkler-simulation-app sprinkler-event-source sprinkler-decision-processor sprinkler-data-sink sprinkler-report"
for IMG in $IMAGES; do
    echo "Pushing $IMG"
    docker push "ghcr.io/corneil/$IMG:latest"
    echo "Pushed $IMG"
done
