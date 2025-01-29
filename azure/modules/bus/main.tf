resource "azurerm_servicebus_namespace" "example" {
  name                         = "bus-${var.workload}"
  location                     = var.location
  resource_group_name          = var.resource_group_name
  sku                          = var.bus_sku
  capacity                     = var.bus_sku == "Premium" ? var.bus_capacity : 0
  premium_messaging_partitions = var.bus_sku == "Premium" ? var.premium_messaging_partitions : 0

  identity {
    type = "UserAssigned"
    identity_ids = [
      var.user_assigned_identity_id
    ]
  }
}
