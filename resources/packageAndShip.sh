helm package .helm
http -f DELETE http://10.174.0.12:8088/api/charts/${CONTAINER_IMAGE}/${CONTAINER_VERSION}
http -f POST http://10.174.0.12:8088/api/charts chart@${CONTAINER_IMAGE}-${CONTAINER_VERSION}.tgz --ignore-stdin