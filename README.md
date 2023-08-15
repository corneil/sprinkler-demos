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

After deploying dataflow run the following to deploy the application the provides various endpoints to support the simulation.

```shell
export NS=<dataflow-namespace>
./deploy/deploy-app.sh
```
`NS` is exported by `use-mk-docker.sh`

## Register Streams Apps

This will register 3 applications:

* Source - sprinkler-event
* Processor - sprinkler-decision
* Sink - sprinkler-data

```shell
export DATAFLOW_VERSION=2.11.0-RC1
./deploy/register-apps.sh
```

## Deploy a Stream
This will create and deploy a stream with the definition `sprinkler-event | sprinkler-decision | sprinkler-data`

```shell
export DATAFLOW_VERSION=2.11.0-RC1
./deploy/deploy-stream.sh
```
