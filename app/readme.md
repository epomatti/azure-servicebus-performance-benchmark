


```properties
app.servicebus.queue=benchmark-queue
app.servicebus.connection_string=Endpoint=sb://{BUS_NAME}.servicebus.windows.net/;SharedAccessKeyName=RootManageSharedAccessKey;SharedAccessKey={KEY}
app.servicebus.max_concurrent_calls=100
app.servicebus.prefetch_count=100
```


```sh
mvn install

# Must be greater than "maxConcurrentCalls"
mvn exec:java -Dreactor.schedulers.defaultBoundedElasticSize=300
```




```sh
location="brazilsouth"
group="rg-benchmark"
namespace="bus-<YOUR NAMESPACE NAME>"

az group create -n $group -l $location
az servicebus namespace create --sku "Standard" -n $namespace -g $group -l $location
az servicebus queue create -n "benchmark-queue" --namespace-name $namespace -g $group --enable-partitioning

az servicebus namespace authorization-rule keys list -g $group --namespace-name $namespace --name "RootManageSharedAccessKey" --query "primaryConnectionString" -o tsv
```