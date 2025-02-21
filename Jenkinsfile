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
                    withCredentials([file(credentialsId: 'wms-secret', variable: 'SECRET_FILE')]) {
                        def secretFileExists = fileExists("${SECRET_FILE}")
                        if (secretFileExists) {
                            sh """
                                echo "Secret file found: ${SECRET_FILE}"
                                cp -v "${SECRET_FILE}" "${RESOURCE_DIR}/application-prod.yml"
                                ls -l "${RESOURCE_DIR}/application-prod.yml"
                                cat "${RESOURCE_DIR}/application-prod.yml" | head -n 5
                            """
                        } else {
                            error "ERROR: Secret file not found at ${SECRET_FILE}"
                        }
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
                    sh """
                        set -x
                        docker build -f ./docker/Dockerfile -t ${DOCKER_TAG} .
                        docker images
                        docker ps -a
                    """
                }
            }
        }

        stage('Deploy to Backend Server') {
            steps {
                script {
                    def currentEnv = sh(
                        script: """
                            ssh -o StrictHostKeyChecking=no ec2-user@api.stockholmes.store '
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
                    def deployEnv = currentEnv == 'blue' ? 'green' : 'blue'
                    def port = deployEnv == 'blue' ? '8011' : '8012'

                    sh """
                        ssh -o StrictHostKeyChecking=no ec2-user@api.stockholmes.store '
                            cd /home/ec2-user/backend

                            # 기존 컨테이너 정리
                            docker ps -a | grep ${port} | grep "Exited" | awk "{print \$1}" | xargs -r docker rm

                            # 현재 실행 중인 컨테이너 중지
                            docker-compose -p spring-wms-${deployEnv} -f docker-compose.${deployEnv}.yml down || true
                        '

                        # 도커 이미지 전송
                        docker save ${DOCKER_TAG} | ssh -o StrictHostKeyChecking=no ec2-user@api.stockholmes.store 'docker load'

                        # 새 컨테이너 시작
                        ssh -o StrictHostKeyChecking=no ec2-user@api.stockholmes.store '
                            cd /home/ec2-user/backend
                            export BUILD_NUMBER=${BUILD_NUMBER}
                            docker-compose -p spring-wms-${deployEnv} -f docker-compose.${deployEnv}.yml up -d

                            sleep 10
                        '

                        # Nginx 설정 업데이트
                        ssh -o StrictHostKeyChecking=no ec2-user@ip-172-31-43-48 "
                            sudo sed -i 's/set \\\$deployment_env \\\".*\\\";/set \\\$deployment_env \\\"${deployEnv}\\\";/' /etc/nginx/conf.d/backend.conf
                            echo '${deployEnv}' | sudo tee /etc/nginx/deployment_env
                            sudo nginx -t && sudo systemctl reload nginx
                        "

                        # 이전 환경 정리
                        if [ '${currentEnv}' != 'none' ]; then
                            ssh -o StrictHostKeyChecking=no ec2-user@api.stockholmes.store '
                                cd /home/ec2-user/backend
                                docker-compose -p spring-wms-${currentEnv} -f docker-compose.${currentEnv}.yml down
                            '
                        fi
                    """
                }
            }
        }
    }
//     post {
//         success {
//             slackSend (
//                 message: """
//                     :white_check_mark: 배포 성공 ! :white_check_mark:
//
//                     *Job*: ${env.JOB_NAME} [${env.BUILD_NUMBER}]
//                     *빌드 URL*: <${env.BUILD_URL}|링크>
//                     *최근 커밋 메시지*: ${env.GIT_COMMIT_MESSAGE}
//                 """
//             )
//         }
//
//         failure {
//             slackSend (
//                 message: """
//                     :x: 배포 실패 :x:
//
//                     *Job*: ${env.JOB_NAME} [${env.BUILD_NUMBER}]
//                     *빌드 URL*: <${env.BUILD_URL}|링크>
//                     *최근 커밋 메시지*: ${env.GIT_COMMIT_MESSAGE}
//                 """
//             )
//         }
//     }
}