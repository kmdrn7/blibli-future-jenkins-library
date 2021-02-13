echo "Running as $(whoami) user."
echo ${DOCKER_PASSWORD} | docker login -u ${DOCKER_USER} --password-stdin
docker build . \
  --tag ${CONTAINER_REGISTRY}/${CONTAINER_IMAGE}:${CONTAINER_VERSION} \
  --tag ${CONTAINER_REGISTRY}/${CONTAINER_IMAGE}:latest
docker push ${CONTAINER_REGISTRY}/${CONTAINER_IMAGE}:${CONTAINER_VERSION}