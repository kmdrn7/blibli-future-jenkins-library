kubectl config set-cluster k8s --server=${KUBE_ENDPOINT} --insecure-skip-tls-verify=true \
&& kubectl config set-credentials jenkins --token=${KUBE_TOKEN} \
&& kubectl config set-context default --cluster=k8s --user=jenkins \
&& kubectl config use-context default
helm package .helm
http -f DELETE http://10.174.0.12:8088/api/charts/${CONTAINER_IMAGE}/${CONTAINER_VERSION}
http -f POST http://10.174.0.12:8088/api/charts chart@${CONTAINER_IMAGE}-${CONTAINER_VERSION}.tgz --ignore-stdin