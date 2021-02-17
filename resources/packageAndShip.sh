helm package .helm
http -f DELETE http://helm.andikaahmad.com:8088/api/charts/${CONTAINER_IMAGE}/${CONTAINER_VERSION}
http -f POST http://helm.andikaahmad.com:8088/api/charts chart@${CONTAINER_IMAGE}-${CONTAINER_VERSION}.tgz --ignore-stdin