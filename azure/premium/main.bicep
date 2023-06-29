targetScope = 'subscription'

@description('Location for all resources.')
param rgLocation string

resource rg 'Microsoft.Resources/resourceGroups@2022-09-01' = {
  name: 'rg-servicebus-benchmark-premium'
  location: rgLocation
}

module network './network.bicep' = {
  name: 'networkDeploment'
  scope: rg
  params: {
    location: rg.location
  }
}

module bus './servicebus.bicep' = {
  name: 'servicebusPremiumDeployment'
  scope: rg
  params: {
    location: rg.location
  }
}

module privatelink './privatelink.bicep' = {
  name: 'privateLinkDeployment'
  scope: rg
  params: {
    location: rg.location
    vnetId: network.outputs.vnetId
    subnetId: network.outputs.subnetId
    namespaceId: bus.outputs.namespaceId
  }
}
