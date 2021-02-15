helm upgrade \
  --install ${CONTAINER_IMAGE} http://10.174.0.12:8088/charts/${CONTAINER_IMAGE}-${CONTAINER_VERSION}.tgz \
  --set image.name=${CONTAINER_REGISTRY}/${CONTAINER_IMAGE}