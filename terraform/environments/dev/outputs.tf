output "namespace" {
  value = kubernetes_namespace.online_spaza.metadata[0].name
}

output "argocd_namespace" {
  value = helm_release.argocd.namespace
}