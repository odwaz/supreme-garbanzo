terraform {
  required_version = ">= 1.0"
  required_providers {
    kubernetes = {
      source  = "hashicorp/kubernetes"
      version = "~> 2.23"
    }
    helm = {
      source  = "hashicorp/helm"
      version = "~> 2.11"
    }
  }
}

provider "kubernetes" {
  config_path = "~/.kube/config"
}

provider "helm" {
  kubernetes {
    config_path = "~/.kube/config"
  }
}

# Namespace
resource "kubernetes_namespace" "online_spaza" {
  metadata {
    name = var.namespace
  }
}

# Secrets
resource "kubernetes_secret" "db_credentials" {
  metadata {
    name      = "db-credentials"
    namespace = kubernetes_namespace.online_spaza.metadata[0].name
  }

  data = {
    username = var.db_username
    password = var.db_password
  }
}

resource "kubernetes_secret" "jwt_secret" {
  metadata {
    name      = "jwt-secret"
    namespace = kubernetes_namespace.online_spaza.metadata[0].name
  }

  data = {
    secret = var.jwt_secret
  }
}

resource "kubernetes_secret" "db_root" {
  metadata {
    name      = "db-root"
    namespace = kubernetes_namespace.online_spaza.metadata[0].name
  }

  data = {
    password = var.db_root_password
  }
}

resource "kubernetes_secret" "stripe_webhook" {
  metadata {
    name      = "stripe-webhook"
    namespace = kubernetes_namespace.online_spaza.metadata[0].name
  }

  data = {
    secret = var.stripe_webhook_secret
  }
}

# ArgoCD Installation
resource "helm_release" "argocd" {
  name             = "argocd"
  repository       = "https://argoproj.github.io/argo-helm"

  chart            = "argo-cd"
  namespace        = "argocd"
  create_namespace = true
  version          = "5.51.6"

values = [
   file("${path.module}/argocd-values.yaml")
  ]
}

# ArgoCD Application
# ArgoCD Application
resource "kubernetes_manifest" "argocd_app" {
  manifest = {
    apiVersion = "argoproj.io/v1alpha1"
    kind       = "Application"
    metadata = {
      name      = "online-spaza-dev"
      namespace = "argocd"
    }
    spec = {
      project = "default"
      source = {
        repoURL        = var.git_repo_url
        targetRevision = var.git_branch
        path           = "k8s"
      }
      destination = {
        server    = "https://kubernetes.default.svc"
        namespace = kubernetes_namespace.online_spaza.metadata[0].name
      }
      syncPolicy = {
        automated = {
          prune    = true
          selfHeal = true
        }
        syncOptions = [
          "CreateNamespace=true"
        ]
      }
    }
  }

  computed_fields = ["metadata.annotations", "metadata.labels"]

  depends_on = [helm_release.argocd]
}
