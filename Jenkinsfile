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
  - name: docker
    image: docker:20.10.14-dind
    securityContext:
      privileged: true
    env:
      - name: DOCKER_TLS_CERTDIR
        value: ""
    volumeMounts:
    - name: workspace-volume
      mountPath: /home/jenkins/agent
    - name: docker-graph-storage
      mountPath: /var/lib/docker
  - name: builder
    image: eclipse-temurin:17-jdk
    command:
    - sleep
    args:
    - 99d
    volumeMounts:
    - name: workspace-volume
      mountPath: /home/jenkins/agent
    env:
    - name: DOCKER_HOST
      value: tcp://localhost:2375
    - name: TESTCONTAINERS_DOCKER_SOCKET_OVERRIDE
      value: tcp://localhost:2375
    - name: TESTCONTAINERS_RYUK_DISABLED
      value: "true"
  volumes:
  - name: workspace-volume
    emptyDir: {}
  - name: docker-graph-storage
    emptyDir: {}
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
                    ./gradlew clean test -Dtestcontainers.ryuk.disabled=true
                    '''
                }
            }
        }
    }
}