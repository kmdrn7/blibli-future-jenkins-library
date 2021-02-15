# ---
# Setup Kubernetes Cluster ----------------------------------------------------------------
kubectl config set-cluster k8s --server=${KUBE_ENDPOINT} --insecure-skip-tls-verify=true \
&& kubectl config set-credentials jenkins --token=${KUBE_TOKEN} \
&& kubectl config set-context default --cluster=k8s --user=jenkins \
&& kubectl config use-context default
# ---
# Create namespace if not exist -----------------------------------------------------------
kubectl create namespace ${NAMESPACE}