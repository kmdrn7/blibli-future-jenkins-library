#!/usr/bin/env groovy
def call(Map params){
    pipeline {
        agent any
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
                    sh 'sh scripts/deliver.sh ${params.server}'
                }
            }
        }
    }
}