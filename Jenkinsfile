pipeline {
    agent any
    parameters {
        choice(
            name: 'DEPLOY_ENV',
            choices: ['auto', 'blue', 'green'],
            description: '배포 환경 선택 (auto는 자동으로 전환)'
        )
    }
    environment {
        DOCKER_TAG = "backend:${BUILD_NUMBER}"
        RESOURCE_DIR = "./src/main/resources"
        // 환경변수를 작은따옴표로 감싸서 정의
        BACKEND_SERVER = 'ec2-user@api.stockholmes.store'
        NGINX_SERVER = 'ec2-user@ip-172-31-43-48'
        BLUE_PORT = '8011'
        GREEN_PORT = '8012'
    }
    tools {
        gradle 'gradle 7.6.1'
    }
    stages {
        // Checkout stage for debugging environment variables
        stage('Checkout and Debug') {
            steps {
                script {
                    echo "===== Stage: Checkout and Debug ====="
                    checkout scm

                    // 환경변수 디버깅
                    echo "Debugging environment variables:"
                    echo "BACKEND_SERVER: ${env.BACKEND_SERVER}"
                    echo "NGINX_SERVER: ${env.NGINX_SERVER}"
                    echo "BLUE_PORT: ${env.BLUE_PORT}"
                    echo "GREEN_PORT: ${env.GREEN_PORT}"

                    sh 'mkdir -p ${RESOURCE_DIR}'
                    sh 'pwd && ls -la'
                }
            }
        }

        // ... (다른 스테이지들은 동일하게 유지)

        stage('Deploy to Backend Server') {
            steps {
                script {
                    // 환경변수 스코프 확인을 위한 로깅
                    echo "Using BACKEND_SERVER: ${env.BACKEND_SERVER}"

                    def currentEnv = sh(
                        script: """
                            ssh -o StrictHostKeyChecking=no \${BACKEND_SERVER} '
                                if docker ps | grep -q "spring-wms-blue"; then
                                    echo "blue"
                                elif docker ps | grep -q "spring-wms-green"; then
                                    echo "green"
                                else
                                    echo "none"
                                fi
                            '
                        """,
                        returnStdout: true
                    ).trim()

                    echo "Current environment: ${currentEnv}"

                    def deployEnv = params.DEPLOY_ENV
                    if (deployEnv == 'auto') {
                        deployEnv = (currentEnv == 'blue') ? 'green' : 'blue'
                    }
                    def port = (deployEnv == 'blue') ? env.BLUE_PORT : env.GREEN_PORT

                    echo "Deploying to ${deployEnv} environment"

                    try {
                        // Stop and clean up target environment first
                        sh """
                            ssh -o StrictHostKeyChecking=no \${BACKEND_SERVER} '
                                cd /home/ec2-user/backend
                                docker-compose -p spring-wms-${deployEnv} -f docker-compose.${deployEnv}.yml down || true
                                docker ps -a | grep "${port}" | grep "Exited" | awk '{print \$1}' | xargs -r docker rm
                            '
                        """

                        // Transfer and deploy new image
                        sh """
                            docker save \${DOCKER_TAG} | ssh -o StrictHostKeyChecking=no \${BACKEND_SERVER} 'docker load'

                            ssh -o StrictHostKeyChecking=no \${BACKEND_SERVER} '
                                cd /home/ec2-user/backend
                                export BUILD_NUMBER=${BUILD_NUMBER}
                                docker-compose -p spring-wms-${deployEnv} -f docker-compose.${deployEnv}.yml up -d
                            '
                        """

                        // Health check
                        def healthCheck = sh(
                            script: """
                                ssh -o StrictHostKeyChecking=no \${BACKEND_SERVER} '
                                    for i in {1..30}; do
                                        if curl -s http://localhost:${port}/actuator/health | grep -q "\\"status\\":\\"UP\\""; then
                                            echo "success"
                                            exit 0
                                        fi
                                        sleep 2
                                    done
                                    echo "failure"
                                    exit 1
                                '
                            """,
                            returnStdout: true
                        ).trim()

                        if (healthCheck == "success") {
                            // Update nginx configuration
                            sh """
                                ssh -o StrictHostKeyChecking=no \${NGINX_SERVER} "
                                    sudo sed -i 's/set \\\$deployment_env \\\".*\\\";/set \\\$deployment_env \\\"${deployEnv}\\\";/' /etc/nginx/conf.d/backend.conf
                                    echo '${deployEnv}' | sudo tee /etc/nginx/deployment_env
                                    sudo nginx -t && sudo systemctl reload nginx
                                "
                            """

                            // Stop previous environment
                            if (currentEnv != 'none' && currentEnv != deployEnv) {
                                sh """
                                    ssh -o StrictHostKeyChecking=no \${BACKEND_SERVER} '
                                        cd /home/ec2-user/backend
                                        docker-compose -p spring-wms-${currentEnv} -f docker-compose.${currentEnv}.yml down
                                    '
                                """
                            }
                        } else {
                            error "New environment health check failed"
                        }
                    } catch (Exception e) {
                        echo "Deployment failed: ${e.message}"
                        // Cleanup failed deployment
                        sh """
                            ssh -o StrictHostKeyChecking=no \${BACKEND_SERVER} '
                                cd /home/ec2-user/backend
                                docker-compose -p spring-wms-${deployEnv} -f docker-compose.${deployEnv}.yml down || true
                            '
                        """

                        if (currentEnv != 'none') {
                            echo "Rolling back to previous environment: ${currentEnv}"
                            sh """
                                ssh -o StrictHostKeyChecking=no \${NGINX_SERVER} "
                                    sudo sed -i 's/set \\\$deployment_env \\\".*\\\";/set \\\$deployment_env \\\"${currentEnv}\\\";/' /etc/nginx/conf.d/backend.conf
                                    echo '${currentEnv}' | sudo tee /etc/nginx/deployment_env
                                    sudo nginx -t && sudo systemctl reload nginx
                                "
                            """
                        }
                        throw e
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
            slackSend (
                message: """
                    :x: 배포 실패 :x:

                    *Job*: ${env.JOB_NAME} [${env.BUILD_NUMBER}]
                    *빌드 URL*: <${env.BUILD_URL}|링크>
                    *최근 커밋 메시지*: ${env.GIT_COMMIT_MESSAGE}
                """
            )
        }
    }
}