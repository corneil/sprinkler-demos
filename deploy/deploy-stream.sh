#!/usr/bin/env bash
SCDIR=$(dirname "$(readlink -f "${BASH_SOURCE[0]}")")
cat > deploy-sprinkler-simulation.shell <<EOF
stream create --name sprinklers --definition "sprinkler-event | sprinkler-decision | sprinkler-data"
stream deploy --name sprinklers
EOF

"$SCDIR/shell/shell.sh" --spring.shell.commandFile=deploy-sprinkler-simulation.shell
