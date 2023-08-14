#!/usr/bin/env bash
SCDIR=$(dirname "$(readlink -f "${BASH_SOURCE[0]}")")
SCDF="$(realpath $SCDIR/../../spring-cloud-dataflow)"
$SCDF/src/deploy/k8s/load-image.sh sprinkler-demos/sprinkler-simulation-app 1.0.0-SNAPSHOT true
kubectl delete deployment sprinkler-app --namespace $NS
kubectl delete service sprinkler-app --namespace $NS
kubectl delete configmap sprinkler-app --namespace $NS
kubectl create --namespace $NS -f $SCDIR/simulation-app.yml
kubectl rollout status deployment --namespace "$NS" sprinkler-app
