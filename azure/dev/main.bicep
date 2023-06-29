targetScope = 'subscription'

param rgLocation string

resource rg 'Microsoft.Resources/resourceGroups@2022-09-01' = {
  name: 'rg-servicebus-benchmark'
  location: rgLocation
}

module bus './servicebus.bicep' = {
  name: 'servicebusDeployment'
  scope: rg
  params: {
    location: rg.location
  }
}
