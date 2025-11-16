# Online Spaza Microservices

## Architecture
- **API Gateway** (8080) - Routes all requests
- **Catalog Service** (8081) - Product management
- **User Auth Service** (8082) - Admin/Merchant authentication
- **Customer Service** (8085) - Customer auth & cart management
- **Order Service** (8087) - Order processing

## Prerequisites
- Docker
- Kubernetes (kubectl)
- MySQL database

## Docker Build

### Build All Services
```bash
# Build from root
mvn clean package -DskipTests

# Build individual Docker images
docker build -t online-spaza/api-gateway:latest ./api-gateway
docker build -t online-spaza/catalog-service:latest ./catalog-service
docker build -t online-spaza/user-auth-service:latest ./user-auth-service
docker build -t online-spaza/customer-service:latest ./customer-service
docker build -t online-spaza/order-service:latest ./order-service
```

### Create Dockerfiles
Each service needs a `Dockerfile`:

```dockerfile
FROM openjdk:11-jre-slim
WORKDIR /app
COPY target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
```

## Kubernetes Deployment

### 1. Create Namespace
```bash
kubectl create namespace online-spaza
```

### 2. Deploy MySQL
```bash
kubectl apply -f k8s/mysql-deployment.yaml
```

### 3. Deploy Services
```bash
kubectl apply -f k8s/api-gateway-deployment.yaml
kubectl apply -f k8s/catalog-service-deployment.yaml
kubectl apply -f k8s/user-auth-service-deployment.yaml
kubectl apply -f k8s/customer-service-deployment.yaml
kubectl apply -f k8s/order-service-deployment.yaml
```

### 4. Verify Deployment
```bash
kubectl get pods -n online-spaza
kubectl get services -n online-spaza
```

### 5. Access API Gateway
```bash
kubectl port-forward -n online-spaza service/api-gateway 8080:8080
```

## Environment Variables
Set these in your k8s deployments:
- `DB_PASSWORD` - MySQL password
- `JWT_SECRET` - JWT signing key
- `SPRING_DATASOURCE_URL` - Database connection string

## Service Endpoints
- API Gateway: `http://localhost:8080`
- Products: `/api/v1/products/**`
- Customer Auth: `/api/v1/customer/**`
- Orders: `/api/v1/auth/orders/**`
- Admin Login: `/api/v1/private/login`
