variable "do_token" {}

variable "do_settings" {
  default = {
    region = "sfo2"
    image  = "ubuntu-18-04-x64"
  }
}

provider "digitalocean" {
  token = var.do_token
}

resource "digitalocean_droplet" "api_server" {
  name   = "tbhs-api"
  image  = var.do_settings.image
  region = var.do_settings.region
  size   = "s-1vcpu-1gb"
}

resource "digitalocean_droplet" "server_runtime" {
  name   = "tbhs-runtime"
  image  = var.do_settings.image
  region = var.do_settings.region
  size   = "s-1vcpu-2gb"
}
