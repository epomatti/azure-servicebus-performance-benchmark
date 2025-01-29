### General ###
location                   = "eastus2"
workload                   = "workload"
public_ip_address_to_allow = ""
subscription_id            = ""


### Key Vault ###
# Required for disk encryption by the Key Vault
keyvault_purge_protection_enabled = false

# AKV Premium
keyvault_sku_name = "premium"
keyvault_key_type = "RSA-HSM"
keyvault_key_size = 4096


### Virtual Machine ###
vm_admin_username  = "azureuser"
vm_public_key_path = ".keys/tmp_rsa.pub"
vm_size            = "Standard_B2ls_v2"

vm_identity_type        = "UserAssigned"           # Options: "SystemAssigned", "UserAssigned", "SystemAssigned, UserAssigned"
vm_role_definition_name = "Key Vault Secrets User" # The role assigned to the VM identity

vm_image_publisher = "canonical"
vm_image_offer     = "ubuntu-24_04-lts"
vm_image_sku       = "server"
vm_image_version   = "latest"
