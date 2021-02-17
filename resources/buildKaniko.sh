echo "Running as $(whoami) user."
echo '{"auths":{"https://index.docker.io/v1/":{"auth":"'$(echo -n ${DOCKER_USER}:${DOCKER_PASSWORD} | base64)'"}}}' > /kaniko/.docker/config.json
/kaniko/executor \
  --dockerfile `pwd`/Dockerfile \
  --context `pwd` \
  --destination=${CONTAINER_REGISTRY}/${CONTAINER_IMAGE}:${CONTAINER_VERSION}
  --destination=${CONTAINER_REGISTRY}/${CONTAINER_IMAGE}:latest