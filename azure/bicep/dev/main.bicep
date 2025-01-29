targetScope = 'subscription'

@description('Location for all resources.')
param rgLocation string

resource rg 'Microsoft.Resources/resourceGroups@2022-09-01' = {
  name: 'rg-servicebus-benchmark-dev'
  location: rgLocation
}

module bus './servicebus.bicep' = {
  name: 'servicebusDevDeployment'
  scope: rg
  params: {
    location: rg.location
  }
}
