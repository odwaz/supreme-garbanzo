variable "namespace" {
  description = "Kubernetes namespace"
  type        = string
  default     = "online-spaza"
}

variable "db_username" {
  description = "Database username"
  type        = string
  default     = "root"
}

variable "db_password" {
  description = "Database password"
  type        = string
  sensitive   = true
}

variable "db_root_password" {
  description = "Database root password"
  type        = string
  sensitive   = true
}

variable "jwt_secret" {
  description = "JWT secret key"
  type        = string
  sensitive   = true
}

variable "stripe_webhook_secret" {
  description = "Stripe webhook secret"
  type        = string
  default     = "REPLACE_ME_STRIPE_WEBHOOK_SECRET"
}

variable "git_repo_url" {
  description = "GitLab repository URL"
  type        = string
  default     = "https://gitlab.com/devwork-pipeline/supreme-garbanzo.git"
}

variable "git_branch" {
  description = "Git branch to track"
  type        = string
  default     = "dev"
}
