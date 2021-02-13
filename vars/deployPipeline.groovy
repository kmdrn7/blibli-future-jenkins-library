#!/usr/bin/env groovy
def call(Map params){
    def build = libraryResource 'build.sh'
    def deliver = libraryResource 'deliver.sh'
    pipeline {
        agent any
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
                            sh(build)
                        }
                    }
                }
            }
            stage('Package Application with HELM') {
                agent {
                    docker {
                        image 'kmdr7/helm-kubectl:latest'
                    }
                }
                steps {
                    withCredentials([
                        usernamePassword(
                            credentialsId: 'cred-kubernetes',
                            usernameVariable: 'KUBE_ENDPOINT',
                            passwordVariable: 'KUBE_TOKEN'
                        )
                    ]) {
                        sh '''
                        kubectl config set-cluster k8s --server="${KUBE_ENDPOINT}" \
                        && kubectl config set-credentials jenkins --token="${KUBE_TOKEN}" \
                        && kubectl config set-context default --cluster=k8s --user=jenkins \
                        && kubectl config use-context default'
                        '''
                    }
                }
            }
        }
    }
}