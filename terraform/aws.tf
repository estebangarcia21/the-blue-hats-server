variable "aws_access_key" {}
variable "aws_secret_key" {}

locals {
  aws_tags = {
    Owner   = "Im-Stevemmmmm"
    Project = "TheBlueHatsServer"
  }
}

provider "aws" {
  region     = "us-west-2"
  access_key = var.aws_access_key
  secret_key = var.aws_secret_key
}

resource "aws_ecr_repository" "runtime_images" {
  name = "runtime-images"
}

resource "aws_s3_bucket" "database_backups" {
  bucket = "database-backups"
  acl    = "private"

  tags = local.aws_tags
}
