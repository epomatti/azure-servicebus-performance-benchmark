param location string = resourceGroup().location

resource serviceBusNamespace 'Microsoft.ServiceBus/namespaces@2022-01-01-preview' = {
  name: 'bus-benchmark-999-premium'
  location: location
  sku: {
    name: 'Premium'
  }
}

resource serviceBusQueue 'Microsoft.ServiceBus/namespaces/queues@2022-01-01-preview' = {
  parent: serviceBusNamespace
  name: 'benchmark-queue'
  properties: {
    enablePartitioning: true
    maxSizeInMegabytes: 81920
  }
}

