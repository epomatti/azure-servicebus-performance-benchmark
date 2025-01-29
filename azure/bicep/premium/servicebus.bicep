@description('Location for all resources.')
param location string

resource serviceBusNamespace 'Microsoft.ServiceBus/namespaces@2022-01-01-preview' = {
  name: 'bus-benchmark-999-premium'
  location: location
  sku: {
    name: 'Premium'
    tier: 'Premium'
    capacity: 1
  }
}

resource serviceBusQueue 'Microsoft.ServiceBus/namespaces/queues@2022-01-01-preview' = {
  parent: serviceBusNamespace
  name: 'benchmark-queue'
  properties: {
    enablePartitioning: false
    maxSizeInMegabytes: 81920
  }
}

output namespaceId string = serviceBusNamespace.id
