# Azure Service Bus Benchmark

Create the `app.properties`:

```sh
$ touch app.properties
```

Enter the properties:

```properties
app.servicebus.connection_string=Endpoint=sb://{BUS_NAME}.servicebus.windows.net/;SharedAccessKeyName=RootManageSharedAccessKey;SharedAccessKey={KEY}
app.servicebus.queue=benchmark-queue
app.servicebus.max_concurrent_calls=100
app.servicebus.prefetch_count=100

app.init_consumer=true
app.init_sender=true
app.sender_threads=100
app.message_quantity=10000
app.message_body_bytes=1024
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
az servicebus queue create -n "benchmark-queue" --namespace-name $namespace -g $group --enable-partitioning true

az servicebus namespace authorization-rule keys list -g $group --namespace-name $namespace --name "RootManageSharedAccessKey" --query "primaryConnectionString" -o tsv
```




```sh
az vm create -n "vm-benchmark" -g "rg-benchmark" --location "brazilsouth" --image "UbuntuLTS" --custom-data "cloud-init.sh" --size "Standard_F8s_v2"
```

```sh
ssh pomatti@<public-ip>
```

Check if cloud init ran correctly:

```sh
java --version
```

If Java is not installed, check cloud init logs or install `cloud-init.sh` manually.


```sh
location="brazilsouth"
group="rg-benchmark"
namespace="bus-benchmark-999-premium"

az servicebus namespace create --sku "Premium" -n $namespace -g $group -l $location
az servicebus queue create -n "benchmark-queue" --namespace-name $namespace -g $group --max-size 5120 --enable-partitioning true

az servicebus namespace authorization-rule keys list -g $group --namespace-name $namespace --name "RootManageSharedAccessKey" --query "primaryConnectionString" -o tsv
```

For better performance, add a Private Endpoint.