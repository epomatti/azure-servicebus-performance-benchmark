variable "workload" {
  type = string
}

variable "resource_group_name" {
  type = string
}

variable "location" {
  type = string
}

variable "bus_sku" {
  type = string
}

variable "bus_capacity" {
  type = number
}

variable "premium_messaging_partitions" {
  type = number
}
