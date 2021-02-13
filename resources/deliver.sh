kubectl config set-cluster k8s --server=${KUBE_ENDPOINT} --insecure-skip-tls-verify=true \
&& kubectl config set-credentials jenkins --token=${KUBE_TOKEN} \
&& kubectl config set-context default --cluster=k8s --user=jenkins \
&& kubectl config use-context default
helm upgrade \
  --install ${CONTAINER_IMAGE} http://10.174.0.12:8088/charts/${CONTAINER_IMAGE}-${CONTAINER_VERSION}.tgz \
  --set image.name=${CONTAINER_REGISTRY}/${CONTAINER_IMAGE}