# sprinkler-demos
Stream Demonstration using Sprinkler Simulation

## Prepare Minikube

```shell
export DATAFLOW_VERSION=2.11.0-RC1
source ../spring-cloud-dataflow/src/deploy/k8s/use-mk-docker.sh rabbit mariadb
../spring-cloud-dataflow/src/deploy/k8s/install-scdf.sh
./build.sh
```
## Build

```shell
./build.sh
```

## Deployment

After deploying dataflow
```shell
export NS=<dataflow-namespace>
./deploy/deploy-app.sh
```
`NS` is exported by `use-mk-docker.sh`

## Register Streams Apps

```shell
export DATAFLOW_VERSION=2.11.0-RC1
./deploy/register-apps.sh
```
