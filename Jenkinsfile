pipeline {
    agent any
    parameters {
        choice(
            name: 'DEPLOY_ENV',
            choices: ['blue', 'green'],
            description: '배포 환경 선택'
        )
    }
    environment {
        DOCKER_TAG = "backend:${BUILD_NUMBER}"
        RESOURCE_DIR = "./src/main/resources"
    }
    tools {
        gradle 'gradle 7.6.1'
    }
    stages {
        stage('Checkout') {
            steps {
                script {
                    echo "===== Stage: Checkout ====="
                    checkout scm
                    sh 'mkdir -p ${RESOURCE_DIR}'
                    sh 'pwd && ls -la'
                }
            }
        }

        stage('Get Commit Message') {
            steps {
                script {
                    echo "===== Stage: Get Commit Message ====="
                    def gitCommitMessage = sh(
                        script: "git log -1 --pretty=%B",
                        returnStdout: true
                    ).trim()
                    echo "Commit Message: ${gitCommitMessage}"
                    echo "Branch Name: ${env.BRANCH_NAME}"
                    env.GIT_COMMIT_MESSAGE = gitCommitMessage
                }
            }
        }

        stage('Prepare') {
            steps {
                script {
                    echo "===== Stage: Prepare ====="
                    sh 'gradle clean --no-daemon'
                    sh 'gradle --version'
                }
            }
        }

        stage('Replace Prod Properties') {
            steps {
                script {
                    echo "===== Stage: Replace Prod Properties ====="
                    sh "chmod -R 777 ${RESOURCE_DIR} || mkdir -p ${RESOURCE_DIR} && chmod -R 777 ${RESOURCE_DIR}"
                    withCredentials([file(credentialsId: 'wms-secret', variable: 'SECRET_FILE')]) {
                        sh '''
                            if [ -f "${SECRET_FILE}" ]; then
                                echo "Secret file found"
                                cp "${SECRET_FILE}" "${RESOURCE_DIR}/application-prod.yml"
                                ls -l "${RESOURCE_DIR}/application-prod.yml"
                                echo "First 5 lines of configuration file:"
                                head -n 5 "${RESOURCE_DIR}/application-prod.yml"
                            else
                                echo "ERROR: Secret file not found at ${SECRET_FILE}"
                                exit 1
                            fi
                        '''
                    }
                }
            }
        }

        stage('Build') {
            steps {
                script {
                    echo "===== Stage: Build ====="
                    sh '''
                        set -x
                        gradle build -Dspring.profiles.active=prod -x test
                        ls -la build/libs/
                    '''
                }
            }
        }

        stage('Build Docker Image') {
            steps {
                script {
                    echo "===== Stage: Build Docker Image ====="
                    sh '''
                        set -x
                        docker build -f ./docker/Dockerfile -t ${DOCKER_TAG} .
                        docker images
                        docker ps -a
                    '''
                }
            }
        }

        stage('Deploy to Backend Server') {
            steps {
                script {
                    def currentEnv = sh(
                        script: '''
                            ssh -o StrictHostKeyChecking=no ec2-user@api.stockholmes.store '
                                if docker ps | grep -q "spring-wms-blue"; then
                                    echo "blue"
                                elif docker ps | grep -q "spring-wms-green"; then
                                    echo "green"
                                else
                                    echo "none"
                                fi
                            '
                        ''',
                        returnStdout: true
                    ).trim()

                    echo "Current environment: ${currentEnv}"
                    def deployEnv = params.DEPLOY_ENV ?: (currentEnv == 'blue' ? 'green' : 'blue')
                    def port = deployEnv == 'blue' ? '8011' : '8012'

                    // 헬스 체크 함수 정의             def checkContainerHealth(String host, String deployEnv, String port) {
                        def healthCheckScript = """
                            ssh -o StrictHostKeyChecking=no ec2-user@${host} '
                                max_attempts=5
                                attempt=0
                                while [ \\\$attempt -lt \\\$max_attempts ]; do
                                    # /api/health 엔드포인트로 헬스 체크
                                    health_response=\\\$(curl -s -o /dev/null -w "%{http_code}" http://localhost:${port}/api/health)

                                    if [ "\\\$health_response" = "200" ]; then
                                        echo "Container is healthy"
                                        exit 0
                                    fi

                                    attempt=\\\$((attempt + 1))
                                    echo "Health check attempt \\\$attempt failed. Retrying in 10 seconds..."
                                    sleep 10
                                done

                                echo "Container health check failed after \\\$max_attempts attempts"
                                exit 1
                            '
                        """

                        def result = sh(script: healthCheckScript, returnStatus: true)
                        return result == 0
                    }

                    sh """
                        ssh -o StrictHostKeyChecking=no ec2-user@api.stockholmes.store '
                            cd /home/ec2-user/backend

                            # 강제로 모든 중지된 컨테이너 제거
                            docker container prune -f

                            # 특정 포트의 모든 컨테이너 완전 제거
                            docker ps -a | grep "${port}" | awk "{print \\\$1}" | xargs -r docker rm -f

                            # Stop current containers
                            docker-compose -p spring-wms-${deployEnv} -f docker-compose.${deployEnv}.yml down || true
                        '

                        # Transfer docker image
                        docker save ${DOCKER_TAG} | ssh -o StrictHostKeyChecking=no ec2-user@api.stockholmes.store 'docker load'

                        # Start new containers
                        ssh -o StrictHostKeyChecking=no ec2-user@api.stockholmes.store '
                            cd /home/ec2-user/backend
                            export BUILD_NUMBER=${BUILD_NUMBER}
                            docker-compose -p spring-wms-${deployEnv} -f docker-compose.${deployEnv}.yml up -d

                            echo "Waiting for container to start..."
                            sleep 10
                        '

                        # Update Nginx configuration
                        ssh -o StrictHostKeyChecking=no ec2-user@ip-172-31-43-48 "
                            sudo sed -i 's/set \\\$deployment_env \\\".*\\\";/set \\\$deployment_env \\\"${deployEnv}\\\";/' /etc/nginx/conf.d/backend.conf
                            echo '${deployEnv}' | sudo tee /etc/nginx/deployment_env
                            sudo nginx -t && sudo systemctl reload nginx
                        "
                    """

                    // Deployment 후 헬스 체크
                    def isHealthy = checkContainerHealth('api.stockholmes.store', deployEnv, port)

                    if (!isHealthy) {
                        error "Container deployment failed health check"
                    }

                    // 이전 환경 정리
                    if (currentEnv != 'none') {
                        sh """
                            ssh -o StrictHostKeyChecking=no ec2-user@api.stockholmes.store '
                                cd /home/ec2-user/backend
                                docker-compose -p spring-wms-${currentEnv} -f docker-compose.${currentEnv}.yml down
                            '
                        """
                    }
                }
            }
        }
    }

    post {
        success {
            slackSend (
                message: """
                    :white_check_mark: 배포 성공 ! :white_check_mark:

                    *Job*: ${env.JOB_NAME} [${env.BUILD_NUMBER}]
                    *빌드 URL*: <${env.BUILD_URL}|링크>
                    *최근 커밋 메시지*: ${env.GIT_COMMIT_MESSAGE}
                """
            )
        }

        failure {
            script {
                def fullErrorLog = sh(
                    script: "cat ${env.WORKSPACE}/build.log",
                    returnStdout: true
                )

                slackSend (
                    message: """
                    :x: 배포 실패 :x:

                    *Job*: ${env.JOB_NAME} [${env.BUILD_NUMBER}]
                    *빌드 URL*: <${env.BUILD_URL}|링크>
                    *최근 커밋 메시지*: ${env.GIT_COMMIT_MESSAGE}

                    *상세 에러 로그*:
                    ```
                    ${fullErrorLog}
                    ```
                    """
                )
            }
        }
    }
}