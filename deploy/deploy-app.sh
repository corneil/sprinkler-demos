#!/usr/bin/env bash
SCDIR=$(dirname "$(readlink -f "${BASH_SOURCE[0]}")")
set +e
kubectl delete deployment sprinkler-app --namespace $NS
kubectl delete service sprinkler-app --namespace $NS
kubectl delete configmap sprinkler-app --namespace $NS
set -e
$SCDIR/k8s/load-image.sh ghcr.io/corneil/sprinkler-demos/sprinkler-simulation-app:latest true
kubectl create --namespace $NS -f "$SCDIR/simulation-app.yml"
kubectl rollout status deployment --namespace "$NS" sprinkler-app
