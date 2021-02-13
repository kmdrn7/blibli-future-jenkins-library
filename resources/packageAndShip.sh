kubectl config set-cluster k8s --server="${KUBE_ENDPOINT}" \
  && kubectl config set-credentials jenkins --token="${KUBE_TOKEN}" \
  && kubectl config set-context default --cluster=k8s --user=jenkins \
  && kubectl config use-context default