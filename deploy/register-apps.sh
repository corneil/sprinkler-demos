#!/usr/bin/env bash
SCDIR=$(dirname "$(readlink -f "${BASH_SOURCE[0]}")")
SCDF="$(realpath $SCDIR/../../spring-cloud-dataflow)"
if [ "$NS" = "" ]; then
    echo "Please configure your namespace and kubernetes access using one of $SCDIR/k8s/use-* scripts"
    exit 1
fi

if [ "$1" == "tasks" ]; then

$SCDIR/k8s/load-image.sh ghcr.io/corneil/sprinkler-demos/sprinkler-report:latest true

cat > $SCDIR/register-apps.shell <<EOF
app register --name sprinkler-report --type task  --bootVersion 3 --uri docker:ghcr.io/corneil/sprinkler-demos/sprinkler-report:latest --metadata-uri maven:io.spring.sprinkler:sprinkler-report:jar:metadata:1.0.0-SNAPSHOT --force
EOF

else

$SCDIR/k8s/load-image.sh ghcr.io/corneil/sprinkler-demos/sprinkler-data-sink:latest true
$SCDIR/k8s/load-image.sh ghcr.io/corneil/sprinkler-demos/sprinkler-event-source:latest true
$SCDIR/k8s/load-image.sh ghcr.io/corneil/sprinkler-demos/sprinkler-decision-processor:latest true

# source.time=docker:springcloudstream/time-source-rabbit:4.0.0-RC2
#source.time.metadata=maven://org.springframework.cloud.stream.app:time-source-rabbit:jar:metadata:4.0.0-RC2
#source.time.bootVersion=3
#sink.log=docker:springcloudstream/log-sink-rabbit:4.0.0-RC2
#sink.log.metadata=maven://org.springframework.cloud.stream.app:log-sink-rabbit:jar:metadata:4.0.0-SNAPSHOT
#sink.log.bootVersion=3


cat > $SCDIR/register-apps.shell <<EOF
app register --name sprinkler-event    --type source --bootVersion 3 --uri docker:ghcr.io/corneil/sprinkler-demos/sprinkler-event-source:latest --metadata-uri maven:io.spring.sprinkler:sprinkler-event-source:jar:metadata:1.0.0-SNAPSHOT --force
app register --name sprinkler-decision --type processor --bootVersion 3 --uri docker:ghcr.io/corneil/sprinkles-demos/sprinkler-decision-processor:latest --metadata-uri maven:io.spring.sprinkler:sprinkler-decision-processor:jar:metadata:1.0.0-SNAPSHOT  --force
app register --name sprinkler-data     --type sink  --bootVersion 3 --uri docker:ghcr.io/corneil/sprinkler-demos/sprinkler-data-sink:latest --metadata-uri maven:io.spring.sprinkler:sprinkler-data-sink:jar:metadata:1.0.0-SNAPSHOT --force
EOF

fi

"$SCDIR/shell/shell.sh" --spring.shell.commandFile=$SCDIR/register-apps.shell

