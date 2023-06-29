@description('Location for all resources.')
param location string

resource vnet 'Microsoft.Network/virtualNetworks@2022-07-01' = {
  name: 'vnet-benchmark'
  location: location
  properties: {
    addressSpace: {
      addressPrefixes: [
        '10.0.0.0/16'
      ]
    }
  }
}

resource subnet 'Microsoft.Network/virtualNetworks/subnets@2022-07-01' = {
  parent: vnet
  name: 'subnet001'
  properties: {
    addressPrefix: '10.0.0.0/24'
    privateEndpointNetworkPolicies: 'Disabled'
  }
}

output vnetId string = vnet.id
output subnetId string = subnet.id
