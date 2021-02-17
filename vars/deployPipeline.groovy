#!/usr/bin/env groovy
def call(Map params){
    def build = libraryResource 'build.sh'
    def packageAndShip = libraryResource 'packageAndShip.sh'
    def prepareDeliver = libraryResource 'prepareDeliver.sh'
    def deliver = libraryResource 'deliver.sh'
    def kanikoYaml = libraryResource 'kaniko.yaml'
    pipeline {
        agent {
            kubernetes {
                label 'jenkins-agent'
                idleMinutes 10
                defaultContainer 'builder'
                yaml kanikoYaml
            }
        }
        stages {
            stage('Build and Push Docker Image') {
                steps {
                    withCredentials([
                        usernamePassword(
                            credentialsId: 'cred-docker',
                            usernameVariable: 'DOCKER_USER',
                            passwordVariable: 'DOCKER_PASSWORD'
                        )
                    ]) {
                        withEnv([
                            'CONTAINER_REGISTRY='+params.containerRegistry,
                            'CONTAINER_IMAGE='+params.containerImage,
                            'CONTAINER_VERSION='+params.containerVersion,
                        ]){
                            // sh(build)
                            sh """
                            echo '{"auths":{"https://index.docker.io/v1/":{"auth":"'$(echo -n ${DOCKER_USER}:${DOCKER_PASSWORD} | base64)'"}}}' > /kaniko/.docker/config.json;
                            /kaniko/executor \
                                --dockerfile `pwd`/Dockerfile \
                                --context `pwd` \
                                --destination=${CONTAINER_REGISTRY}/${CONTAINER_IMAGE}:${CONTAINER_VERSION}
                                --destination=${CONTAINER_REGISTRY}/${CONTAINER_IMAGE}:latest
                                ;
                            """
                        }
                    }
                }
            }
            // stage('Package Application with HELM') {
            //     agent {
            //         docker {
            //             image 'kmdr7/helm-kubectl:latest'
            //         }
            //     }
            //     steps {
            //         withEnv([
            //             'CONTAINER_REGISTRY='+params.containerRegistry,
            //             'CONTAINER_IMAGE='+params.containerImage,
            //             'CONTAINER_VERSION='+params.containerVersion,
            //         ]){
            //             sh(packageAndShip)
            //         }
            //     }
            // }
            // stage('Deploy Application to Kubernetes') {
            //     agent {
            //         docker {
            //             image 'kmdr7/helm-kubectl:latest'
            //         }
            //     }
            //     steps {
            //         withCredentials([
            //             usernamePassword(
            //                 credentialsId: 'cred-kubernetes',
            //                 usernameVariable: 'KUBE_ENDPOINT',
            //                 passwordVariable: 'KUBE_TOKEN'
            //             )
            //         ]) {
            //             withEnv([
            //                 'CONTAINER_REGISTRY='+params.containerRegistry,
            //                 'CONTAINER_IMAGE='+params.containerImage,
            //                 'CONTAINER_VERSION='+params.containerVersion,
            //                 'NAMESPACE='+params.namespace,
            //             ]){
            //                 sh(prepareDeliver)
            //                 sh(deliver)
            //             }
            //         }
            //     }
            // }
        }
    }
}