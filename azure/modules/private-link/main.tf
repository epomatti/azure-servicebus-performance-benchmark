resource "azurerm_private_dns_zone" "service_bus" {
  name                = "privatelink.servicebus.windows.net"
  resource_group_name = var.resource_group_name
}

resource "azurerm_private_dns_zone_virtual_network_link" "service_bus" {
  name                  = "servicebus-premium-link"
  resource_group_name   = var.resource_group_name
  private_dns_zone_name = azurerm_private_dns_zone.service_bus.name
  virtual_network_id    = var.vnet_id
  registration_enabled  = true
}

resource "azurerm_private_endpoint" "service_bus" {
  name                = "pe-servicebus"
  location            = var.location
  resource_group_name = var.resource_group_name
  subnet_id           = var.private_endpoints_subnet_id

  private_dns_zone_group {
    name = azurerm_private_dns_zone.service_bus.name

    private_dns_zone_ids = [
      azurerm_private_dns_zone.service_bus.id
    ]
  }

  private_service_connection {
    name                           = "servicebus"
    private_connection_resource_id = var.service_bus_namespace_id
    is_manual_connection           = false
    subresource_names              = ["namespace"]
  }
}
