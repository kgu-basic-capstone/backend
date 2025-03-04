pipeline {
    agent {
        kubernetes {
            defaultContainer 'jnlp'
            yaml """
apiVersion: v1
kind: Pod
spec:
  containers:
  - name: jnlp
    image: jenkins/inbound-agent:latest
    volumeMounts:
    - name: workspace-volume
      mountPath: /home/jenkins/agent
  - name: builder
    image: eclipse-temurin:17-jdk
    command:
    - sleep
    args:
    - 99d
    volumeMounts:
    - name: workspace-volume
      mountPath: /home/jenkins/agent
    - name: docker-sock
      mountPath: /var/run/docker.sock
    env:
    - name: DOCKER_HOST
      value: unix:///var/run/docker.sock
    - name: TESTCONTAINERS_DOCKER_SOCKET_OVERRIDE
      value: /var/run/docker.sock
  volumes:
  - name: workspace-volume
    emptyDir: {}
  - name: docker-sock
    hostPath:
      path: /var/run/docker.sock
"""
        }
    }

    options {
        timeout(time: 30, unit: 'MINUTES')
        disableConcurrentBuilds()
    }

    environment {
        GITHUB_CREDENTIALS = credentials('github-credentials')
        GITHUB_REPOSITORY = "kgu-basic-capstone/backend"
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Test with Testcontainers') {
            steps {
                container('builder') {
                    sh '''
                    chmod +x ./gradlew
                    ./gradlew clean test
                    '''
                }
            }
        }
    }
}