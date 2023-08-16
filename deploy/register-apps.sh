#!/usr/bin/env bash
SCDIR=$(dirname "$(readlink -f "${BASH_SOURCE[0]}")")
SCDF="$(realpath $SCDIR/../../spring-cloud-dataflow)"
if [ "$NS" = "" ]; then
    echo "Please configure your namespace and kubernetes access using one of $SCDF/src/deploy/k8s/use-* scripts"
    exit 1
fi

if [ "$1" == "tasks" ]; then

$SCDF/src/deploy/k8s/load-image.sh corneil/sprinkler-report latest true
# --metadata-uri maven:io.spring.sprinkler:sprinkler-report:jar:metadata:latest
cat > $SCDIR/register-apps.shell <<EOF
app register --name sprinkler-report --uri docker:corneil/sprinkler-report:latest --type task  --bootVersion 3 --force
EOF

else

$SCDF/src/deploy/k8s/load-image.sh corneil/sprinkler-data-sink latest true
$SCDF/src/deploy/k8s/load-image.sh corneil/sprinkler-event-source latest true
$SCDF/src/deploy/k8s/load-image.sh corneil/sprinkler-decision-processor latest true

# --metadata-uri maven:io.spring.sprinkler:sprinkler-event-source:jar:metadata:latest
# --metadata-uri maven:io.spring.sprinkler:sprinkler-decision-processor:jar:metadata:latest
# --metadata-uri maven:io.spring.sprinkler:sprinkler-data-sink:jar:metadata:latest
# --metadata-uri maven:io.spring.sprinkler:sprinkler-report:jar:metadata:latest

cat > $SCDIR/register-apps.shell <<EOF
app register --name sprinkler-event --uri docker:corneil/sprinkler-event-source:latest --type source --bootVersion 3 --force
app register --name sprinkler-decision --uri docker:corneil/sprinkler-decision-processor:latest --type processor  --bootVersion 3 --force
app register --name sprinkler-data --uri docker:corneil/sprinkler-data-sink:latest --type sink  --bootVersion 3 --force
EOF

fi

"$SCDF/src/deploy/shell/shell.sh" --spring.shell.commandFile=$SCDIR/register-apps.shell

