# Azure Service Bus Benchmark

Benchmarking sample code for Service Bus using the Java SDK.

Recommended requirements are JDK 17 and Maven latest version.

## 💻 Local Development

Start by creating the Service Bus namespace:

```sh
location="brazilsouth"
group="rg-benchmark"
namespace="bus-benchmark-999" # change to a unique name

az group create -n $group -l $location
az servicebus namespace create --sku "Standard" -n $namespace -g $group -l $location
az servicebus queue create -n "benchmark-queue" --namespace-name $namespace -g $group --enable-partitioning true

az servicebus namespace authorization-rule keys list -g $group --namespace-name $namespace --name "RootManageSharedAccessKey" --query "primaryConnectionString" -o tsv
```

Create the `app.properties` in the root folder:

```sh
$ touch app.properties
```

Add the required properties to the file:

```properties
# Connectivity
app.servicebus.connection_string=Endpoint=sb://{BUS_NAME}.servicebus.windows.net/;SharedAccessKeyName=RootManageSharedAccessKey;SharedAccessKey={KEY}
app.servicebus.queue=benchmark-queue

# Control active modules
app.init_sender=true
app.init_consumer=false

# Producer / Sender
app.sender_concurrent_clients=3
app.sender_threads=10
app.message_quantity=10000
app.message_body_bytes=1024

# Consumer
app.servicebus.concurrent_clients=5
app.servicebus.max_concurrent_calls=50
app.servicebus.prefetch_count=100
```

Start the app:

```sh
mvn install
mvn exec:java -Dreactor.schedulers.defaultBoundedElasticSize=100
```

> ℹ️ Due to [this known issue](https://github.com/Azure/azure-sdk-for-java/issues/30483), `defaultBoundedElasticSize` needs to be set for Reactor. Value must be greater than "maxConcurrentCalls".


## 🚀 Cloud Benchmark

Ramp up a jump box VM for dedicated performance:

```sh
az vm create -n "vm-benchmark" -g "rg-benchmark" --location "brazilsouth" --image "UbuntuLTS" --custom-data cloud-init.sh --size "Standard_F8s_v2" --public-ip-sku "Standard"
```

Connect to the VM:

```sh
ssh <user>@<public-ip>
```

Check if the cloud-init script ran correctly:

```sh
java --version
```

If Java is not installed, check cloud init logs (/var/log/cloud-init-output.log) or install [`cloud-init.sh`](./cloud-init.sh) manually.

To load Maven in to the session:

```sh
source /etc/profile.d/maven.sh
```

Clone the application from GitHub. You'll need an SSH Key or login with credentials.

Create a **Premium** namespace:

```sh
location="brazilsouth"
group="rg-benchmark"
namespace="bus-benchmark-999-premium"

az servicebus namespace create --sku "Premium" -n $namespace -g $group -l $location
az servicebus queue create -n "benchmark-queue" --namespace-name $namespace -g $group --max-size 5120 --enable-partitioning true

az servicebus namespace authorization-rule keys list -g $group --namespace-name $namespace --name "RootManageSharedAccessKey" --query "primaryConnectionString" -o tsv
```

Create the `app.properties` file as explained in the previous section.

For better performance, add a [Private Endpoint](https://learn.microsoft.com/en-us/azure/service-bus-messaging/private-link-service) and attach it to the VM subnet.

> ℹ️ When using Private Endpoints you don't need to change the URL. Use the same public FQDN, Azure will take care of the routing. Test it with `nslookup`.

To control Java memory and other fine-tunning configurations:

```sh
export MAVEN_OPTS="-Xms256m -Xmx12g"
```

```sh
mvn install
mvn exec:java -Dlogback.configurationFile="logback-benchmark.xml" -Dreactor.schedulers.defaultBoundedElasticSize=1200
```

## 📈 Benchmark Results



## References

- [Service Bus Messaging Exceptions](https://learn.microsoft.com/en-us/azure/service-bus-messaging/service-bus-messaging-exceptions)
- [Service Bus Java SDK 7.11.0](https://azuresdkdocs.blob.core.windows.net/$web/java/azure-messaging-servicebus/7.11.0/index.html)
- [Service Bus Java SDK](https://learn.microsoft.com/en-us/java/api/overview/azure/messaging-servicebus-readme?view=azure-java-stable)
- [Private Link Service](https://learn.microsoft.com/en-us/azure/service-bus-messaging/private-link-service)
