helm upgrade \
  --install ${CONTAINER_IMAGE} http://helm.andikaahmad.com:8088/charts/${CONTAINER_IMAGE}-${CONTAINER_VERSION}.tgz \
  --set image.name=${CONTAINER_REGISTRY}/${CONTAINER_IMAGE}