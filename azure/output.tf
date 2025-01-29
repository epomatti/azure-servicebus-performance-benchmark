output "vm_ssh_command" {
  value = "ssh -i .keys/temp_rsa ${var.vm_admin_username}@${module.virtual_machine.public_ip_address}"
}
