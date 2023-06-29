# Azure Service Bus Benchmark

Benchmarking the performance of Azure Service Bus using the Java SDK.

## 💻 Local Development

Use this section to run the code locally prior to running it in the cloud.

Start by creating the Service Bus namespace:

```sh
az deployment sub create \
  --location brazilsouth \
  --template-file azure/dev/main.bicep \
  --parameters rgLocation=brazilsouth
```

Get the connection string for the namespace:

```sh
az servicebus namespace authorization-rule keys list -g "rg-servicebus-benchmark" --namespace-name "bus-benchmark-999" --name "RootManageSharedAccessKey" --query "primaryConnectionString" -o tsv
```

Create the `app.properties` in the root folder:

```sh
cp config/template.app.properties app.properties
```

Update the `app.servicebus.connection_string` property with the real connection string.

Install the latest stable Java:

```sh
sdk install maven
sdk install java 17.0.7-tem
```

Start the app:

```sh
mvn install
mvn exec:java -Dreactor.schedulers.defaultBoundedElasticSize=100
```

> ℹ️ Due to [this known issue](https://github.com/Azure/azure-sdk-for-java/issues/30483), `defaultBoundedElasticSize` needs to be set for Reactor. Value must be greater than "maxConcurrentCalls".


## 🚀 Cloud Benchmark

Run the benchmark in the cloud with a Premium namespace.

Ramp up a jump box VM for dedicated performance:

```sh
az vm create -n "vm-benchmark" -g "rg-servicebus-benchmark" --location "brazilsouth" --image "UbuntuLTS" --custom-data cloud-init.sh --size "Standard_D8s_v4" --public-ip-sku "Standard"
```

Check if the cloud-init script executed correctly:

```sh
cloud-init status
```

Clone the application from GitHub in the VM. You'll need an SSH Key or login with credentials.

Create the **Premium** namespace:

```sh
az deployment group create --resource-group powerapps --template-file main.bicep

az servicebus namespace authorization-rule keys list -g $group --namespace-name $namespace --name "RootManageSharedAccessKey" --query "primaryConnectionString" -o tsv
```

For better performance, add a [Private Endpoint](https://learn.microsoft.com/en-us/azure/service-bus-messaging/private-link-service) and attach it to the VM subnet.

> ℹ️ When using Private Endpoints you don't need to change the URL. Use the same public FQDN, Azure will take care of the routing. Test it with `nslookup`.

To control Java memory and other fine-tunning configurations:

```sh
export MAVEN_OPTS="-Xms256m -Xmx16g"
```

Create the `app.properties` file as explained in the previous section. Tune the concurrency according to your requirements.

Run the application:

```sh
mvn install
mvn exec:java -Dlogback.configurationFile="logback-benchmark.xml" -Dreactor.schedulers.defaultBoundedElasticSize=1200
```

## 📈 Benchmarking Results

While sending messages to a Premium namespace with 1x MU it was possible to achieve:
- 5.000+ messages / second for single messages
- 10.000+ messages / send for batch messages

Message count:

<img src=".assets/sender_benchmark.png" width=300 />

Namespace resources:

<img src=".assets/sender_resources.png" width=400 />

## References

- [Service Bus Messaging Exceptions](https://learn.microsoft.com/en-us/azure/service-bus-messaging/service-bus-messaging-exceptions)
- [Service Bus Java SDK 7.11.0 Javadocs](https://azuresdkdocs.blob.core.windows.net/$web/java/azure-messaging-servicebus/7.11.0/index.html)
- [Service Bus Java SDK Guidelines](https://learn.microsoft.com/en-us/java/api/overview/azure/messaging-servicebus-readme?view=azure-java-stable)
- [Private Link Service](https://learn.microsoft.com/en-us/azure/service-bus-messaging/private-link-service)
