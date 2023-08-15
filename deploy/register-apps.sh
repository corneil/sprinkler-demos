#!/usr/bin/env bash
SCDIR=$(dirname "$(readlink -f "${BASH_SOURCE[0]}")")
SCDF="$(realpath $SCDIR/../../spring-cloud-dataflow)"
$SCDF/src/deploy/k8s/load-image.sh corneil/sprinkler-data-sink latest true
$SCDF/src/deploy/k8s/load-image.sh corneil/sprinkler-timer-source latest true
$SCDF/src/deploy/k8s/load-image.sh corneil/sprinkler-decision-processor latest true
$SCDF/src/deploy/k8s/load-image.sh corneil/sprinkler-report latest true

cat > $SCDIR/register-apps.shell <<EOF
app register --name sprinkler-timer --uri docker:corneil/sprinkler-timer-source:latest --metadata-uri maven:io.spring.sprinkler:sprinkler-timer-source:jar:metadata:latest --type source --bootVersion 3 --force
app register --name sprinkler-decision --uri docker:corneil/sprinkler-decision-processor:latest --metadata-uri maven:io.spring.sprinkler:sprinkler-decision-processor:jar:metadata:latest --type processor  --bootVersion 3 --force
app register --name sprinkler-data --uri docker:corneil/sprinkler-data-sink:latest --metadata-uri maven:io.spring.sprinkler:sprinkler-data-sink:jar:metadata:latest --type sink  --bootVersion 3 --force
app register --name sprinkler-report --uri docker:corneil/sprinkler-report:latest --metadata-uri maven:io.spring.sprinkler:sprinkler-report:jar:metadata:latest --type task  --bootVersion 3 --force
EOF

"$SCDF/src/deploy/shell/shell.sh" --spring.shell.commandFile=$SCDIR/register-apps.shell

