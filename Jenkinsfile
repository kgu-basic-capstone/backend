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
            post {
                always {
                    junit '**/build/test-results/**/*.xml'

                    script {
                        def prNumber = ""
                        if (env.CHANGE_ID) {
                            prNumber = env.CHANGE_ID
                        } else {
                            def gitInfo = sh(script: 'git log -1 --pretty=%B', returnStdout: true).trim()
                            def prMatch = gitInfo =~ /Merge pull request #(\d+)/
                            if (prMatch) {
                                prNumber = prMatch[0][1]
                            }
                        }

                        if (prNumber) {
                            def testSummary = sh(script: '''
                                if [ -d "build/test-results/" ]; then
                                  echo "### 테스트 결과 요약"
                                  echo "✅ 전체 테스트: $(grep -r "tests=" build/test-results/ | awk -F "tests=" '{sum += $2; gsub(/[^0-9].*/, "", $2); sum += $2} END {print sum}')"
                                  echo "❌ 실패한 테스트: $(grep -r "failures=" build/test-results/ | awk -F "failures=" '{sum += $2; gsub(/[^0-9].*/, "", $2); sum += $2} END {print sum}')"
                                  echo "⚠️ 스킵된 테스트: $(grep -r "skipped=" build/test-results/ | awk -F "skipped=" '{sum += $2; gsub(/[^0-9].*/, "", $2); sum += $2} END {print sum}')"
                                  echo "⏱️ 총 소요 시간: $(grep -r "time=" build/test-results/ | awk -F "time=" '{sum += $2; gsub(/[^0-9.].*/, "", $2); sum += $2} END {print sum}') 초"
                                else
                                  echo "### 테스트 결과 없음"
                                  echo "테스트 결과 파일을 찾을 수 없습니다."
                                fi
                            ''', returnStdout: true).trim()

                            def failedTests = sh(script: '''
                                if [ -d "build/test-results/" ]; then
                                  grep -r "failure message" build/test-results/ | awk -F "failure message=" '{print "* " $2}' | sed 's/"//g' || true
                                fi
                            ''', returnStdout: true).trim()

                            if (failedTests) {
                                testSummary += "\n\n### ❌ 실패한 테스트 상세 정보\n" + failedTests
                            }

                            withCredentials([usernamePassword(credentialsId: 'github-credentials', passwordVariable: 'GITHUB_TOKEN', usernameVariable: 'GITHUB_USERNAME')]) {
                                sh """
                                    curl -X POST \
                                    -H "Authorization: token ${GITHUB_TOKEN}" \
                                    -H "Accept: application/vnd.github.v3+json" \
                                    https://api.github.com/repos/${GITHUB_REPOSITORY}/issues/${prNumber}/comments \
                                    -d '{"body": "## 테스트 결과 보고 (Build #${BUILD_NUMBER})\n\n${testSummary.replace('\n', '\\n').replace('"', '\\"')}"}'
                                """
                            }
                        }
                    }
                }
            }
        }
    }

    post {
        success {
            script {
                if (env.CHANGE_ID) {
                    withCredentials([usernamePassword(credentialsId: 'github-credentials', passwordVariable: 'GITHUB_TOKEN', usernameVariable: 'GITHUB_USERNAME')]) {
                        sh """
                            curl -X POST \
                            -H "Authorization: token ${GITHUB_TOKEN}" \
                            -H "Accept: application/vnd.github.v3+json" \
                            https://api.github.com/repos/${GITHUB_REPOSITORY}/statuses/${GIT_COMMIT} \
                            -d '{"state": "success", "context": "jenkins/tests", "description": "All tests passed", "target_url": "${BUILD_URL}"}'
                        """
                    }
                }
            }
        }
        failure {
            script {
                if (env.CHANGE_ID) {
                    withCredentials([usernamePassword(credentialsId: 'github-credentials', passwordVariable: 'GITHUB_TOKEN', usernameVariable: 'GITHUB_USERNAME')]) {
                        sh """
                            curl -X POST \
                            -H "Authorization: token ${GITHUB_TOKEN}" \
                            -H "Accept: application/vnd.github.v3+json" \
                            https://api.github.com/repos/${GITHUB_REPOSITORY}/statuses/${GIT_COMMIT} \
                            -d '{"state": "failure", "context": "jenkins/tests", "description": "Tests failed", "target_url": "${BUILD_URL}"}'
                        """
                    }
                }
            }
        }
        cleanup {
            cleanWs()
        }
    }
}