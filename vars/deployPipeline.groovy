#!/usr/bin/env groovy
def call(Map params){
    def build = libraryResource 'build.sh'
    def buildKaniko = libraryResource 'buildKaniko.sh'
    def packageAndShip = libraryResource 'packageAndShip.sh'
    def prepareDeliver = libraryResource 'prepareDeliver.sh'
    def deliver = libraryResource 'deliver.sh'
    def podYaml = libraryResource 'pod.yaml'
    pipeline {
        agent {
            kubernetes {
                label 'jenkins-agent'
                idleMinutes 10
                defaultContainer 'docker'
                yaml podYaml
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
                            sh(build)
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