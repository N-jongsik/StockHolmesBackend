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
    }
    tools {
        gradle 'gradle 8.11.1'
    }
    stages {
        stage('Checkout') {
            steps {
                script {
                    echo "===== Stage: Checkout ====="
                    checkout scm
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
                    echo "Cleaning Gradle Project"
                    sh 'gradle clean --no-daemon'
                    echo "Checking Gradle version"
                    sh 'gradle --version'
                    echo "Listing project files"
                    sh 'ls -la'
                }
            }
        }

        stage('Replace Prod Properties') {
            steps {
                script {
                    echo "===== Stage: Replace Prod Properties ====="
                    echo "Current directory structure:"
                    sh 'pwd && ls -la'

                    withCredentials([file(credentialsId: 'wms-secret', variable: 'SECRET_FILE')]) {
                        echo "Copying secret file..."
                        sh """
                            echo "Resource directory contents before:"
                            ls -la ./src/main/resources/

                            chmod -R 777 ./src/main/resources
                            cp \${SECRET_FILE} ./src/main/resources/application-secret.yml

                            echo "Resource directory contents after:"
                            ls -la ./src/main/resources/

                            # 파일 존재 확인 & 내용 체크
                            echo "===== Verifying secret file ====="
                            if [ -f ./src/main/resources/application-secret.yml ]; then
                                echo "Secret file exists"
                                echo "File permissions:"
                                ls -l ./src/main/resources/application-secret.yml
                                echo "File size:"
                                stat -f %z ./src/main/resources/application-secret.yml || stat -c %s ./src/main/resources/application-secret.yml
                                echo "First line of file (without sensitive data):"
                                head -n 1 ./src/main/resources/application-secret.yml
                            else
                                echo "ERROR: Secret file was not copied properly"
                            fi
                        """
                    }
                }
            }
        }

        stage('Build') {
            steps {
                script {
                    echo "===== Stage: Build ====="
                    echo "Starting Gradle build..."
                    sh '''
                        set -x
                        gradle build -x test
                        echo "Build directory contents:"
                        ls -la build/libs/
                    '''
                }
            }
        }

        stage('Build Docker Image') {
            steps {
                script {
                    echo "===== Stage: Build Docker Image ====="
                    echo "Current directory structure:"
                    sh 'pwd && ls -la'

                    echo "Docker file contents:"
                    sh 'cat ./docker/Dockerfile'

                    echo "Building Docker image..."
                    sh """
                        set -x
                        docker build -f ./docker/Dockerfile -t ${DOCKER_TAG} .
                        echo "===== Docker Images ====="
                        docker images
                        echo "===== Docker Processes ====="
                        docker ps -a
                    """
                }
            }
        }
        stage('Deploy to Backend Server') {
            steps {
                script {
                    withCredentials([
                        string(credentialsId: 'redis-host', variable: 'REDIS_HOST'),
                        string(credentialsId: 'redis-password', variable: 'REDIS_PASSWORD')
                    ]) {
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
                        def containerName = "spring-wms-${deployEnv}"

                        echo "Deploying to environment: ${deployEnv}"
                        echo "Using port: ${port}"

                        sshPublisher(publishers: [
                            sshPublisherDesc(
                                configName: 'BackendServer',
                                transfers: [
                                    sshTransfer(
                                        execCommand: """
                                            set -e
                                            set -x

                                            cd /home/ec2-user/backend

                                            # 권한 변경 추가
                                            sudo chmod 666 /home/ec2-user/backend/docker-compose.blue.yml
                                            sudo chmod 666 /home/ec2-user/backend/docker-compose.green.yml

                                            # Docker Compose 파일 이미지 태그 업데이트
                                            sed -i 's/image: backend:.*/image: backend:${BUILD_NUMBER}/' docker-compose.${deployEnv}.yml

                                            # Redis 호스트 환경변수 업데이트
                                            sed -i 's/SPRING_DATA_REDIS_HOST=.*/SPRING_DATA_REDIS_HOST=${REDIS_HOST}/' docker-compose.${deployEnv}.yml

                                            # Docker Compose 파일 내용 확인
                                            cat docker-compose.${deployEnv}.yml

                                            # 기존 컨테이너 중지
                                            docker-compose -p spring-wms-${deployEnv} -f docker-compose.${deployEnv}.yml down || true

                                            # 새 컨테이너 시작
                                            docker-compose -p spring-wms-${deployEnv} -f docker-compose.${deployEnv}.yml up -d

                                            # 컨테이너 시작 대기
                                            sleep 10

                                            # Nginx 설정 업데이트
                                            ssh ec2-user@ip-172-31-43-48 "sudo sed -i 's/set \\\$deployment_env \".*\";/set \\\$deployment_env \"${deployEnv}\";/' /etc/nginx/conf.d/backend.conf"
                                            ssh ec2-user@ip-172-31-43-48 'echo "${deployEnv}" | sudo tee /etc/nginx/deployment_env'

                                            # Nginx 리로드
                                            sudo nginx -t && sudo systemctl reload nginx

                                            # 이전 환경 컨테이너 중지
                                            if [ "${currentEnv}" != "none" ]; then
                                                docker-compose -p spring-wms-${currentEnv} -f docker-compose.${currentEnv}.yml down
                                            fi
                                        """
                                    )
                                ],
                                verbose: true
                            )
                        ])
                    }
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