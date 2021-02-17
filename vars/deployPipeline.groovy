#!/usr/bin/env groovy
def call(Map params){
    def build = libraryResource 'build.sh'
    def packageAndShip = libraryResource 'packageAndShip.sh'
    def prepareDeliver = libraryResource 'prepareDeliver.sh'
    def deliver = libraryResource 'deliver.sh'
    def podYaml = libraryResource 'pod.yaml'
    pipeline {
        agent {
            kubernetes {
                label 'jenkins-slave'
                defaultContainer 'docker'
                yaml podYaml
            }
        }
        stages {
            stage('Build and Push Docker Image') {
                steps {
                    container('docker') {
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
            }
            stage('Package Application with HELM') {
                steps {
                    container('helm-kubectl') {
                        withEnv([
                            'CONTAINER_REGISTRY='+params.containerRegistry,
                            'CONTAINER_IMAGE='+params.containerImage,
                            'CONTAINER_VERSION='+params.containerVersion,
                        ]){
                            sh(packageAndShip)
                        }
                    }
                }
            }
            stage('Deploy Application to Kubernetes') {
                steps {
                    container('helm-kubectl') {
                        withCredentials([
                            usernamePassword(
                                credentialsId: 'cred-kubernetes',
                                usernameVariable: 'KUBE_ENDPOINT',
                                passwordVariable: 'KUBE_TOKEN'
                            )
                        ]) {
                            withEnv([
                                'CONTAINER_REGISTRY='+params.containerRegistry,
                                'CONTAINER_IMAGE='+params.containerImage,
                                'CONTAINER_VERSION='+params.containerVersion,
                                'NAMESPACE='+params.namespace,
                            ]){
                                sh(prepareDeliver)
                                sh(deliver)
                            }
                        }
                    }
                }
            }
        }
    }
}