@description('Location for all resources.')
param location string

@description('The VNET ID to associate with Private Link')
param vnetId string

@description('The subnet ID to associate the private endpoint')
param subnetId string

@description('The subnet ID to associate the private endpoint')
param namespaceId string

resource privateEndpoint 'Microsoft.Network/privateEndpoints@2023-04-01' = {
  name: 'pe-benchmark'
  location: location
  properties: {
    subnet: {
      id: subnetId
    }
    privateLinkServiceConnections: [
      {
        name: 'pe-benchmark'
        properties: {
          privateLinkServiceId: namespaceId
          groupIds: [
            'namespace'
          ]
        }
      }
    ]
  }
}

resource privateDnsZone 'Microsoft.Network/privateDnsZones@2020-06-01' = {
  name: 'privatelink.servicebus.windows.net'
  location: 'global'
  properties: {}
}

resource privateDnsZoneLink 'Microsoft.Network/privateDnsZones/virtualNetworkLinks@2020-06-01' = {
  parent: privateDnsZone
  name: 'servicebus-premium-link'
  location: 'global'
  properties: {
    registrationEnabled: true
    virtualNetwork: {
      id: vnetId
    }
  }
}

resource pvtEndpointDnsGroup 'Microsoft.Network/privateEndpoints/privateDnsZoneGroups@2022-07-01' = {
  parent: privateEndpoint
  name: 'servicebus'
  properties: {
    privateDnsZoneConfigs: [
      {
        name: 'config1'
        properties: {
          privateDnsZoneId: privateDnsZone.id
        }
      }
    ]
  }
}
