#!/usr/bin/env groovy
def call(Map params){
    def deliver = libraryResource 'deliver.sh'
    pipeline {
        agent {
            node {
                label 'slave'
            }
        }
        stages {
            stage('Build') {
                steps {
                    sh 'mvn -B -DskipTests clean package'
                }
            }
            stage('Test') {
                steps {
                    sh 'mvn test'
                }
                post {
                    always {
                        junit 'target/surefire-reports/*.xml'
                    }
                }
            }
            stage('Deliver') {
                steps {
                    withEnv([
                        'SERVER='+params.server
                    ]){
                        sh(deliver)
                    }
                }
            }
        }
    }
}