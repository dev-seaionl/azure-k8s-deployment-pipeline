
---

# Steps to Implement

### 1. Create a Spring Boot Application
- Generate a Spring Boot project (e.g., using [Spring Initializr](https://start.spring.io/)).

### 2. Prepare Kubernetes Cluster
- Set up an AKS cluster in Azurw or EKS in AWS

### 3. Set Up Azure DevOps
- Create a service connection for the Docker registry.
- Create a service connection for the Kubernetes cluster.

### 4. Push Code to Azure Repos
- Add your application files (`Dockerfile`, `deployment.yaml`, and `azure-pipelines.yml`) to your repository.

### 5. Run the Pipeline
- The pipeline builds and pushes the Docker image, then deploys it to Kubernetes.

---

# Key Updates

### **Readiness Probe**
- Uses an HTTP GET request to check the `/actuator/health` endpoint, a common Spring Boot health check.
- Ensures the container is ready before receiving traffic.

### **Image Version Tag**
- Replaced `latest` with `<version-tag>`. Replace `<version-tag>` with a specific version, e.g., `v1.0.0`.

### **Horizontal Pod Autoscaler (HPA)**
- Added an HPA to scale the deployment based on CPU usage.
- Adjusts the replicas between `2` and `10` depending on average CPU utilization.

### **Liveness Probe**
- **Path**: `/actuator/dependency-health` assumes your Spring Boot application exposes an endpoint that checks the health of the dependent service.
- If this endpoint indicates failure (e.g., by returning a non-2xx HTTP status), Kubernetes will restart the pod.
- Ensure your application has logic to query the dependent service and respond appropriately at this endpoint.

#### **Initial Delay**
- `initialDelaySeconds: 15` ensures Kubernetes waits long enough for the dependent service to start before beginning the liveness checks.

#### **Failure Handling**
- `failureThreshold: 3` restarts the pod only after three consecutive failures.

---

# Instructions

1. **Update your Docker image version** during the build process in Azure DevOps to tag the image with a version instead of `latest`.

2. **Deploy the updated `deployment.yaml`** with your pipeline or manually using the command:

   ```bash
   kubectl apply -f deployment.yaml
   ```

3. **Verify scaling behavior**:
   - Use the command below to check HPA metrics:
     ```bash
     kubectl get hpa
     ```
   - Perform a load test on the application to observe pods scaling up or down.

---
