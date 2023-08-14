#!/usr/bin/env bash
SCDIR=$(dirname "$(readlink -f "${BASH_SOURCE[0]}")")
SCDF="$(realpath $SCDIR/../../spring-cloud-dataflow)"
$SCDF/src/deploy/k8s/load-image.sh sprinkler-demos/sprinkler-data-sink 1.0.0-SNAPSHOT true
$SCDF/src/deploy/k8s/load-image.sh sprinkler-demos/sprinkler-timer-source 1.0.0-SNAPSHOT true
$SCDF/src/deploy/k8s/load-image.sh sprinkler-demos/sprinkler-decision-processor 1.0.0-SNAPSHOT true

cat > $SCDIR/register-apps.shell <<EOF
app register --name sprinkler-timer --uri docker:sprinkler-demos/sprinkler-timer-source:1.0.0-SNAPSHOT --metadata-uri maven:com.example.sprinkler:sprinkler-timer-source:jar:metadata:1.0.0-SNAPSHOT --type source --bootVersion 3 --force
app register --name sprinkler-decision --uri docker:sprinkler-demos/sprinkler-decision-processor:1.0.0-SNAPSHOT --metadata-uri maven:com.example.sprinkler:sprinkler-decision-processor:jar:metadata:1.0.0-SNAPSHOT --type processor  --bootVersion 3 --force
app register --name sprinkler-data --uri docker:sprinkler-demos/sprinkler-data-sink:1.0.0-SNAPSHOT --metadata-uri maven:com.example.sprinkler:sprinkler-data-sink:jar:metadata:1.0.0-SNAPSHOT --type sink  --bootVersion 3 --force
EOF

"$SCDF/src/deploy/shell/shell.sh" --spring.shell.commandFile=$SCDIR/register-apps.shell

