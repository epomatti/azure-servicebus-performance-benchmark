@description('Location for all resources.')
param location string

@description('The subnet ID to associate the private endpoint')
param subnetId string

@description('The admin username for the virtual machine.')
param username string

@description('The password for the admin of the virtual machine.')
@secure()
param password string

resource publicIp 'Microsoft.Network/publicIPAddresses@2022-07-01' = {
  name: 'pip-vm-benchmark'
  location: location
  sku: {
    name: 'Standard'
  }
  properties: {
    publicIPAllocationMethod: 'Static'
  }
}

resource nic 'Microsoft.Network/networkInterfaces@2022-07-01' = {
  name: 'nic-vm-benchmark'
  location: location
  properties: {
    ipConfigurations: [
      {
        name: 'ipconfig1'
        properties: {
          privateIPAllocationMethod: 'Dynamic'
          publicIPAddress: {
            id: publicIp.id
          }
          subnet: {
            id: subnetId
          }
        }
      }
    ]
  }
}

resource vm 'Microsoft.Compute/virtualMachines@2022-11-01' = {
  name: 'vm-benchmark'
  location: location
  properties: {
    userData: loadFileAsBase64('./cloud-init.sh')
    hardwareProfile: {
      vmSize: 'Standard_D8s_v4'
    }
    osProfile: {
      computerName: 'vm-benchmark'
      adminUsername: username
      adminPassword: password
      linuxConfiguration: {
        disablePasswordAuthentication: true
        ssh: {
          publicKeys: [
            {
              keyData: loadFileAsBase64('./id_rsa')
              path: '/home/${username}/.ssh/authorized_keys'
            }
          ]
        }
      }
    }
    storageProfile: {
      imageReference: {
        publisher: 'Canonical'
        offer: '0001-com-ubuntu-server-jammy'
        sku: '22_04-lts-gen2'
        version: 'latest'
      }
      osDisk: {
        name: 'osdisk-vm-benchmark'
        createOption: 'FromImage'
        managedDisk: {
          storageAccountType: 'StandardSSD_LRS'
        }
      }
    }
    networkProfile: {
      networkInterfaces: [
        {
          id: nic.id
        }
      ]
    }
  }
}
