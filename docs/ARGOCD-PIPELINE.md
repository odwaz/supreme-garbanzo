# ArgoCD + GitLab CI/CD Pipeline Documentation

## Table of Contents
1. [Overview](#overview)
2. [Prerequisites](#prerequisites)
3. [Initial Setup](#initial-setup)
4. [ArgoCD Installation](#argocd-installation)
5. [ArgoCD CLI Setup](#argocd-cli-setup)
6. [Adding Applications](#adding-applications)
7. [GitLab CI/CD Configuration](#gitlab-cicd-configuration)
8. [Troubleshooting](#troubleshooting)
9. [Credentials Reference](#credentials-reference)

---

## Overview

This pipeline automates the deployment of microservices using:
- **GitLab CI/CD**: Builds Docker images and pushes to Docker Hub
- **ArgoCD**: Monitors GitLab repository and syncs Kubernetes manifests
- **Kubernetes**: Runs the microservices (Minikube for local development)

**Architecture Flow**:
```
Code Push → GitLab CI/CD → Docker Hub → ArgoCD Detects Changes → K8s Deployment
```

---

## Prerequisites

- **Kubernetes Cluster**: Minikube or any K8s cluster
- **kubectl**: Configured and connected to your cluster
- **Docker Hub Account**: For storing container images
- **GitLab Repository**: With CI/CD enabled
- **Windows PowerShell**: For CLI commands (adjust for Linux/macOS)

---

## Initial Setup

### 1. Create Kubernetes Namespace
```bash
kubectl create namespace online-spaza
```

### 2. Create Secrets
Apply secrets before deploying applications:
```bash
kubectl apply -f k8s/secrets.yaml
```

**secrets.yaml** should contain:
- `db-credentials`: Database username/password
- `db-root`: MySQL root password
- `jwt-secret`: JWT signing key
- `stripe-webhook`: Stripe webhook secret (if applicable)

---

## ArgoCD Installation

### Install ArgoCD
```bash
kubectl create namespace argocd
kubectl apply -n argocd -f https://raw.githubusercontent.com/argoproj/argo-cd/stable/manifests/install.yaml
```

### Wait for ArgoCD Pods
```bash
kubectl wait --for=condition=Ready pods --all -n argocd --timeout=300s
```

### Apply Timeout Configuration (Critical for GitLab)
ArgoCD may timeout when fetching from GitLab. Apply these fixes:

**1. Increase Reconciliation Timeout:**
```bash
kubectl patch configmap argocd-cm -n argocd --type merge -p '{"data":{"timeout.reconciliation":"600s"}}'
```

**2. Set Execution Timeout:**
```bash
kubectl set env deployment/argocd-repo-server -n argocd ARGOCD_EXEC_TIMEOUT=5m
```

**3. Restart Repo Server:**
```bash
kubectl rollout restart deployment argocd-repo-server -n argocd
kubectl rollout status deployment argocd-repo-server -n argocd
```

### Retrieve Admin Password
```bash
kubectl get secret argocd-initial-admin-secret -n argocd -o jsonpath="{.data.password}" | base64 -d
```

**Save this password** - you'll need it for CLI and UI access.

### Access ArgoCD UI
```bash
kubectl port-forward svc/argocd-server -n argocd 8081:443
```

Access at: `https://localhost:8081`
- Username: `admin`
- Password: (from previous step)

---

## ArgoCD CLI Setup

### Install ArgoCD CLI (Windows)

**Option 1: PowerShell Script**
```powershell
$version = (Invoke-RestMethod https://api.github.com/repos/argoproj/argo-cd/releases/latest).tag_name
$url = "https://github.com/argoproj/argo-cd/releases/download/" + $version + "/argocd-windows-amd64.exe"
$output = "$env:USERPROFILE\argocd.exe"
Invoke-WebRequest -Uri $url -OutFile $output
```

**Option 2: Direct Download**
Download from: https://github.com/argoproj/argo-cd/releases/latest

**Installation Location**: `C:\Users\<username>\argocd.exe`

### Login to ArgoCD CLI
```bash
# Start port-forward first (if not already running)
kubectl port-forward svc/argocd-server -n argocd 8081:443

# Login (use your actual password)
argocd login localhost:8081 --username admin --password <YOUR_ADMIN_PASSWORD> --insecure
```

**Example**:
```bash
argocd login localhost:8081 --username admin --password b3xQPKbxkJBGw --insecure
```

---

## Adding Applications

### Step 1: Add Git Repository

**For GitLab with Personal Access Token:**
```bash
argocd repo add <GIT_REPO_URL> \
  --username <GITLAB_USERNAME> \
  --password <GITLAB_TOKEN> \
  --upsert
```

**Example**:
```bash
argocd repo add https://gitlab.com/devwork-pipeline/supreme-garbanzo.git \
  --username odwaz \
  --password glpat-b5IQ9OQXjidOAGsykZdwAG86MQp1OjdzZHY5Cw.01.1219o1zje \
  --upsert
```

**Verify Repository Added:**
```bash
argocd repo list
```

### Step 2: Create ArgoCD Application

**Template:**
```bash
argocd app create <APP_NAME> \
  --repo <GIT_REPO_URL> \
  --path <K8S_MANIFESTS_PATH> \
  --dest-server https://kubernetes.default.svc \
  --dest-namespace <TARGET_NAMESPACE> \
  --revision <GIT_BRANCH> \
  --sync-option CreateNamespace=true
```

**Example - Online Spaza Application:**
```bash
argocd app create online-spaza \
  --repo https://gitlab.com/devwork-pipeline/supreme-garbanzo.git \
  --path k8s \
  --dest-server https://kubernetes.default.svc \
  --dest-namespace online-spaza \
  --revision dev \
  --sync-option CreateNamespace=true
```

**Example - Adding Another Application:**
```bash
argocd app create payment-service \
  --repo https://gitlab.com/devwork-pipeline/supreme-garbanzo.git \
  --path payment-service/k8s \
  --dest-server https://kubernetes.default.svc \
  --dest-namespace payment-service \
  --revision main \
  --sync-option CreateNamespace=true
```

### Step 3: Verify Application

**Get Application Status:**
```bash
argocd app get <APP_NAME>
```

**Example**:
```bash
argocd app get online-spaza
```

**Expected Output**:
- Health Status: `Healthy`
- Sync Status: `OutOfSync` or `Synced`

### Step 4: Sync Application

**Manual Sync:**
```bash
argocd app sync <APP_NAME>
```

**Example**:
```bash
argocd app sync online-spaza
```

**Enable Auto-Sync (Optional):**
```bash
argocd app set <APP_NAME> --sync-policy automated --auto-prune --self-heal
```

**Example**:
```bash
argocd app set online-spaza --sync-policy automated --auto-prune --self-heal
```

### Step 5: Verify Deployment

**Check Pods:**
```bash
kubectl get pods -n <NAMESPACE>
```

**Example**:
```bash
kubectl get pods -n online-spaza
```

**Check Services:**
```bash
kubectl get svc -n <NAMESPACE>
```

---

## GitLab CI/CD Configuration

### .gitlab-ci.yml Structure

Located at repository root: `.gitlab-ci.yml`

**Key Components**:
1. **Build Stage**: Compiles Java code with Maven
2. **Push Stage**: Builds and pushes Docker images to Docker Hub

**Example Configuration**:
```yaml
stages:
  - build
  - push

variables:
  DOCKER_REGISTRY: docker.io
  DOCKER_IMAGE_PREFIX: codecorner
  MAVEN_OPTS: "-Dmaven.repo.local=.m2/repository"

build:
  stage: build
  image: maven:3.9-eclipse-temurin-11
  script:
    - mvn clean package -DskipTests
  artifacts:
    paths:
      - "*/target/*.jar"
    expire_in: 1 hour
  only:
    - dev

push:
  stage: push
  image: docker:24-cli
  services:
    - docker:24-dind
  before_script:
    - echo "$DOCKER_PASSWORD" | docker login -u "$DOCKER_USERNAME" --password-stdin
  script:
    - docker build -t $DOCKER_REGISTRY/$DOCKER_IMAGE_PREFIX/api-gateway:$CI_COMMIT_SHORT_SHA ./api-gateway
    - docker push $DOCKER_REGISTRY/$DOCKER_IMAGE_PREFIX/api-gateway:$CI_COMMIT_SHORT_SHA
    # Repeat for other services
  only:
    - dev
```

### GitLab CI/CD Variables

Set in GitLab: **Settings → CI/CD → Variables**

Required Variables:
- `DOCKER_USERNAME`: Docker Hub username (e.g., `codecorner`)
- `DOCKER_PASSWORD`: Docker Hub password or access token

---

## Troubleshooting

### Issue: Git Fetch Timeout

**Symptoms**:
- ArgoCD application creation hangs
- Logs show: `context deadline exceeded` or `timeout after 100s`

**Solution**:
```bash
# Increase reconciliation timeout
kubectl patch configmap argocd-cm -n argocd --type merge -p '{"data":{"timeout.reconciliation":"600s"}}'

# Set execution timeout
kubectl set env deployment/argocd-repo-server -n argocd ARGOCD_EXEC_TIMEOUT=5m

# Restart repo server
kubectl rollout restart deployment argocd-repo-server -n argocd
```

### Issue: Application Stuck in "Progressing"

**Symptoms**:
- Application health shows `Progressing` indefinitely
- Service type is `LoadBalancer` on Minikube

**Solution**:
Change service type to `ClusterIP` or `NodePort` in deployment YAML:
```yaml
spec:
  type: ClusterIP  # or NodePort
```

### Issue: Repository Not Found

**Symptoms**:
- `repository not found` error when creating application

**Solution**:
```bash
# Re-add repository with --upsert flag
argocd repo add <REPO_URL> --username <USER> --password <TOKEN> --upsert

# Verify repository
argocd repo list
```

### Issue: Pods CrashLoopBackOff

**Symptoms**:
- Pods restart repeatedly
- Logs show missing environment variables or secrets

**Solution**:
```bash
# Verify secrets exist
kubectl get secrets -n <NAMESPACE>

# Check pod logs
kubectl logs <POD_NAME> -n <NAMESPACE>

# Verify secret references in deployment YAML
kubectl describe deployment <DEPLOYMENT_NAME> -n <NAMESPACE>
```

### Issue: ArgoCD CLI Not Found

**Symptoms**:
- `argocd: command not found` in PowerShell

**Solution**:
```powershell
# Use full path
C:\Users\<username>\argocd.exe <command>

# Or use relative path in PowerShell
.\argocd.exe <command>

# Or add to PATH environment variable
```

---

## Credentials Reference

### GitLab
- **Repository**: `https://gitlab.com/devwork-pipeline/supreme-garbanzo.git`
- **Branch**: `dev`
- **Username**: `odwaz`
- **Personal Access Token**: `glpat-b5IQ9OQXjidOAGsykZdwAG86MQp1OjdzZHY5Cw.01.1219o1zje`

### Docker Hub
- **Registry**: `docker.io`
- **Username**: `codecorner`
- **Image Prefix**: `codecorner/<service-name>`

### ArgoCD
- **Namespace**: `argocd`
- **Username**: `admin`
- **Password**: Retrieved via `kubectl get secret argocd-initial-admin-secret`
- **UI Access**: `https://localhost:8081` (via port-forward)

### Kubernetes
- **Application Namespace**: `online-spaza`
- **Cluster**: Minikube (local)
- **Context**: Default from `~/.kube/config`

### Database (MySQL)
- **Username**: `root`
- **Password**: `Qwbrty!`
- **Secret Name**: `db-credentials`, `db-root`

### JWT
- **Secret Key**: `dev-jwt-secret-key-83451237`
- **Secret Name**: `jwt-secret`

---

## Quick Reference Commands

### ArgoCD Application Lifecycle
```bash
# Create application
argocd app create <APP_NAME> --repo <REPO> --path <PATH> --dest-namespace <NS> --revision <BRANCH>

# Get application status
argocd app get <APP_NAME>

# Sync application
argocd app sync <APP_NAME>

# Delete application
argocd app delete <APP_NAME>

# List all applications
argocd app list

# Enable auto-sync
argocd app set <APP_NAME> --sync-policy automated --auto-prune --self-heal

# Disable auto-sync
argocd app set <APP_NAME> --sync-policy none
```

### Kubernetes Verification
```bash
# Check pods
kubectl get pods -n <NAMESPACE>

# Check services
kubectl get svc -n <NAMESPACE>

# Check deployments
kubectl get deployments -n <NAMESPACE>

# View pod logs
kubectl logs <POD_NAME> -n <NAMESPACE>

# Describe pod
kubectl describe pod <POD_NAME> -n <NAMESPACE>

# Port forward to service
kubectl port-forward svc/<SERVICE_NAME> <LOCAL_PORT>:<SERVICE_PORT> -n <NAMESPACE>
```

### ArgoCD Management
```bash
# Port forward to ArgoCD UI
kubectl port-forward svc/argocd-server -n argocd 8081:443

# Get admin password
kubectl get secret argocd-initial-admin-secret -n argocd -o jsonpath="{.data.password}" | base64 -d

# List repositories
argocd repo list

# Add repository
argocd repo add <REPO_URL> --username <USER> --password <TOKEN>

# Remove repository
argocd repo rm <REPO_URL>
```

---

## Adding New Applications - Step-by-Step

### 1. Prepare Application
- Create Kubernetes manifests in `<app-name>/k8s/` directory
- Ensure Dockerfile exists for building image
- Update `.gitlab-ci.yml` to build and push new service image

### 2. Push to GitLab
```bash
git add .
git commit -m "Add new application"
git push origin dev
```

### 3. Create ArgoCD Application
```bash
argocd app create <NEW_APP_NAME> \
  --repo https://gitlab.com/devwork-pipeline/supreme-garbanzo.git \
  --path <NEW_APP_PATH> \
  --dest-server https://kubernetes.default.svc \
  --dest-namespace <NEW_NAMESPACE> \
  --revision dev \
  --sync-option CreateNamespace=true
```

### 4. Sync and Verify
```bash
argocd app sync <NEW_APP_NAME>
argocd app get <NEW_APP_NAME>
kubectl get pods -n <NEW_NAMESPACE>
```

---

## Best Practices

1. **Use Specific Image Tags**: Avoid `:latest`, use commit SHA or version tags
2. **Enable Auto-Sync**: For development environments to reduce manual intervention
3. **Monitor ArgoCD Logs**: Check repo-server logs if sync issues occur
4. **Namespace Isolation**: Use separate namespaces for different applications
5. **Secret Management**: Never commit secrets to Git, use Kubernetes secrets
6. **Health Checks**: Define liveness and readiness probes in deployments
7. **Resource Limits**: Set CPU and memory limits for all containers
8. **GitLab CI/CD**: Use artifacts to pass build outputs between stages
9. **Rollback Strategy**: Keep previous image tags for quick rollbacks
10. **Documentation**: Update this document when adding new applications

---

## Support and Maintenance

### Regular Maintenance Tasks
- **Weekly**: Review ArgoCD application health status
- **Monthly**: Update ArgoCD to latest stable version
- **Quarterly**: Rotate GitLab tokens and update in ArgoCD

### Monitoring
- ArgoCD UI: `https://localhost:8081`
- GitLab CI/CD: `https://gitlab.com/devwork-pipeline/supreme-garbanzo/-/pipelines`
- Kubernetes Pods: `kubectl get pods -n online-spaza`

---

**Last Updated**: 2024
**Maintained By**: DevOps Team
**Version**: 1.0
