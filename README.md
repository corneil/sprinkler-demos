# sprinkler-demos
Stream Demonstration using Sprinkler Simulation


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

## Register Streams App

```shell
export DATAFLOW_VERSION=2.11.0-SNAPSHOT
./deploy/register-apps.sh
```
