#!/usr/bin/env bash
SCDIR=$(dirname "$(readlink -f "${BASH_SOURCE[0]}")")
SCDF="$(realpath $SCDIR/../../spring-cloud-dataflow)"
cat > deploy-sprinkler-simulation.shell <<EOF
stream create --name sprinkler-simulation --definition "sprinkler-event | sprinkler-decision | sprinkler-data"
stream deploy --name sprinkler-simulation
EOF

"$SCDF/src/deploy/shell/shell.sh" --spring.shell.commandFile=deploy-sprinkler-simulation.shell
