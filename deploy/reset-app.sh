#!/usr/bin/env bash
if [ "$SIMULATION_APP" = "" ]; then
    SIMULATION_APP=http://localhost:8080/api
fi
echo "Resetting $SIMULATION_APP"
curl -s -X POST $SIMULATION_APP/reset
