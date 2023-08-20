#!/usr/bin/env bash
SCDIR=$(dirname "$(readlink -f "${BASH_SOURCE[0]}")")
cat > deploy-sprinkler-simulationv2.shell <<EOF
stream update --name sprinkler-simulation --properties app.sprinkler-event.spring.profiles.active=simulation-client,app.sprinkler-decision.spring.profiles.active=weather
EOF

"$SCDIR/shell/shell.sh" --spring.shell.commandFile=deploy-sprinkler-simulationv2.shell
