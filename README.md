# Azure Service Bus - Performance Benchmark

Benchmarking the performance of Azure Service Bus on Standard and Premium tiers using the Java SDK.

## 💻 Local Development

You can use this section to run the code locally, prior to running it in the cloud.

Start by creating the Service Bus namespace using a Standard bus tier:

```sh
# Upgrade
az bicep upgrade

# Create
az deployment sub create \
  --location brazilsouth \
  --template-file azure/bicep/dev/main.bicep \
  --parameters rgLocation=brazilsouth
```

Get the connection string for the namespace:

```sh
az servicebus namespace authorization-rule keys list -g rg-servicebus-benchmark-dev --namespace-name bus-benchmark999-dev --name RootManageSharedAccessKey --query primaryConnectionString -o tsv
```

Create the `app.properties` in the root folder from the template:

```sh
# Open the file and edit the "connection_string" property
cp config/template.app.properties app.properties
```

Run the benchmark client:

```sh
mvn install
mvn exec:java
```

## 🚀 Cloud Benchmark

Run the benchmark in the cloud with a **Premium** namespace.

First, create the Linux VM SSH key pair:

```sh
ssh-keygen -f azure/premium/id_rsa
```

Now create the infrastructure for the benchmark:

```sh
# Upgrade
az bicep upgrade

# Create
az deployment sub create \
  --location brazilsouth \
  --template-file azure/premium/main.bicep \
  --parameters rgLocation=brazilsouth vmUsername=bench vmPassword=p4ssw0rd
```

Once the process is complete, connect to the VM and check if the `cloud-init` script executed correctly:

```sh
ssh -i ./azure/premium/id_rsa bench@<publicIp>

cloud-init status
```

Download and extract the application code from the latest release:

```sh
curl -L https://github.com/epomatti/azure-servicebus-performance-benchmark/archive/refs/tags/v0.0.1.tar.gz -o client.tar.gz
tar -xf client.tar.gz
```

From the application root, create the properties file:

```sh
cp config/template.app.properties app.properties
```

Set up the Service Bus connectivity:

```sh
# Get the connection string (run this in your local machine)
az servicebus namespace authorization-rule keys list -g "rg-servicebus-benchmark-premium" --namespace-name "bus-benchmark-999-premium" --name "RootManageSharedAccessKey" --query "primaryConnectionString" -o tsv

# Edit with the real connection string of the Premium namespace
nano app.properties
```

Change the application properties of the client for a high volume load test. Example:

```
app.message_quantity=1000000
```

To control Java memory and JVM configurations:

```sh
export MAVEN_OPTS="-Xms256m -Xmx16g"
```

Run the application:

```sh
mvn install
mvn exec:java -Dlogback.configurationFile="logback-benchmark.xml" -Dreactor.schedulers.defaultBoundedElasticSize=1200
```

## 📈 Benchmarking Results

Average numbers collected during the tests:

| Tier           | Message Units | Send mode | Avg. messages / Sec |
|----------------|---------------|-----------|----------------|
| Premium        | 1x            | Single    | 5,000          |
| Premium        | 1x            | Batch     | 13,888         |

Namespace resources:

<img src=".assets/sender_resources.png" width=500 />

Sample:

<img src=".assets/sender_benchmark.png" width=800 />

## Troubleshooting

Due to [this issue](https://github.com/Azure/azure-sdk-for-java/issues/30483), `defaultBoundedElasticSize` needed to be set for Reactor. Value must be greater than "maxConcurrentCalls".

This seems to have been resolved, but here is the command for reference if needed:

```sh
mvn exec:java -Dreactor.schedulers.defaultBoundedElasticSize=100
```

## References

- [Service Bus Messaging Exceptions](https://learn.microsoft.com/en-us/azure/service-bus-messaging/service-bus-messaging-exceptions)
- [Service Bus Java SDK 7.11.0 Javadocs](https://azuresdkdocs.blob.core.windows.net/$web/java/azure-messaging-servicebus/7.11.0/index.html)
- [Service Bus Java SDK Guidelines](https://learn.microsoft.com/en-us/java/api/overview/azure/messaging-servicebus-readme?view=azure-java-stable)
- [Private Link Service](https://learn.microsoft.com/en-us/azure/service-bus-messaging/private-link-service)
