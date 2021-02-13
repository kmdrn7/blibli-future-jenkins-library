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
                        sh(build)
                        // withEnv([
                        //     'CONTAINER_REGISTRY='+params.containerRegistry,
                        //     'CONTAINER_IMAGE='+params.containerImage,
                        //     'CONTAINER_VERSION='+params.containerVersion,
                        // ]){
                        // }
                    }
                }
            }
        }
    }
}