#!/bin/bash

set -x

# 작업 디렉토리로 이동
cd /home/ec2-user/backend

# 로그 디렉토리 확인 및 생성
LOG_DIR="./logs"
mkdir -p $LOG_DIR
LOG_FILE="$LOG_DIR/deploy.log"

# 현재 사용 중인 포트 확인하여 환경 결정
if netstat -tulpn | grep -q ':8011'; then
    CURRENT_ENV="blue"
    DEPLOY_ENV="green"
elif netstat -tulpn | grep -q ':8012'; then
    CURRENT_ENV="green"
    DEPLOY_ENV="blue"
else
    CURRENT_ENV="none"
    DEPLOY_ENV="blue"  # 기본값
fi

DOCKER_APP_NAME="spring-wms"

# 환경별 포트 설정
if [ "$DEPLOY_ENV" == "blue" ]; then
    DEPLOY_PORT="8011"
    CURRENT_PORT="8012"
else
    DEPLOY_PORT="8012"
    CURRENT_PORT="8011"
fi

# 배포 시작 로깅
echo "[$(date '+%Y-%m-%d %H:%M:%S')] 현재 환경: ${CURRENT_ENV}" >> $LOG_FILE
echo "[$(date '+%Y-%m-%d %H:%M:%S')] 배포 시작: ${DEPLOY_ENV} 환경 (포트: ${DEPLOY_PORT})" >> $LOG_FILE

# 1. 현재 상태 확인
echo "[$(date '+%Y-%m-%d %H:%M:%S')] 현재 실행 중인 컨테이너:" >> $LOG_FILE
docker ps -a >> $LOG_FILE
echo "[$(date '+%Y-%m-%d %H:%M:%S')] 현재 사용 중인 포트:" >> $LOG_FILE
netstat -tulpn | grep -E ':(8011|8012)' >> $LOG_FILE

# 2. 새 컨테이너 시작
echo "[$(date '+%Y-%m-%d %H:%M:%S')] ${DEPLOY_ENV} 컨테이너 시작 (포트: ${DEPLOY_PORT})" >> $LOG_FILE
docker-compose -p "${DOCKER_APP_NAME}-${DEPLOY_ENV}" -f "docker-compose.${DEPLOY_ENV}.yml" up -d --build || {
    echo "[$(date '+%Y-%m-%d %H:%M:%S')] ${DEPLOY_ENV} 컨테이너 시작 실패" >> $LOG_FILE
    exit 1
}

# 3. 헬스 체크 (최대 60초 대기)
HEALTH_CHECK_TIMEOUT=60
echo "[$(date '+%Y-%m-%d %H:%M:%S')] 헬스 체크 시작: ${DEPLOY_PORT}" >> $LOG_FILE

for i in $(seq 1 $HEALTH_CHECK_TIMEOUT); do
    if curl -f http://localhost:${DEPLOY_PORT}/health; then
        echo "[$(date '+%Y-%m-%d %H:%M:%S')] ${DEPLOY_ENV} 헬스 체크 성공" >> $LOG_FILE
        break
    fi

    if [ $i -eq $HEALTH_CHECK_TIMEOUT ]; then
        echo "[$(date '+%Y-%m-%d %H:%M:%S')] ${DEPLOY_ENV} 헬스 체크 실패" >> $LOG_FILE
        exit 1
    fi

    echo "Waiting for health check... ($i/$HEALTH_CHECK_TIMEOUT)" >> $LOG_FILE
    sleep 1
done

# 4. Nginx 설정 변경
echo "[$(date '+%Y-%m-%d %H:%M:%S')] Nginx 설정 변경" >> $LOG_FILE
echo "set \$target_env ${DEPLOY_ENV};" | sudo tee /etc/nginx/deployment_env

# Nginx 설정 테스트
sudo nginx -t || {
    echo "[$(date '+%Y-%m-%d %H:%M:%S')] Nginx 설정 테스트 실패" >> $LOG_FILE
    exit 1
}

# Nginx 재시작
sudo systemctl restart nginx || {
    echo "[$(date '+%Y-%m-%d %H:%M:%S')] Nginx 재시작 실패" >> $LOG_FILE
    exit 1
}

# 5. 이전 환경 정리 (현재 환경이 있는 경우에만)
if [ "$CURRENT_ENV" != "none" ]; then
    echo "[$(date '+%Y-%m-%d %H:%M:%S')] ${CURRENT_ENV} 환경 정리" >> $LOG_FILE
    docker-compose -p "${DOCKER_APP_NAME}-${CURRENT_ENV}" -f "docker-compose.${CURRENT_ENV}.yml" down || {
        echo "[$(date '+%Y-%m-%d %H:%M:%S')] ${CURRENT_ENV} 컨테이너 중지 실패" >> $LOG_FILE
    }
fi

# 6. Docker 정리
echo "[$(date '+%Y-%m-%d %H:%M:%S')] Docker 이미지 정리" >> $LOG_FILE
docker image prune -af

# 7. 최종 상태 확인
echo "[$(date '+%Y-%m-%d %H:%M:%S')] 최종 상태 확인" >> $LOG_FILE
echo "Docker 컨테이너 상태:" >> $LOG_FILE
docker ps -a >> $LOG_FILE
echo "사용 중인 포트:" >> $LOG_FILE
netstat -tulpn | grep -E ':(8011|8012)' >> $LOG_FILE
echo "Nginx 상태:" >> $LOG_FILE
sudo systemctl status nginx >> $LOG_FILE

# 배포 완료 로깅
echo "[$(date '+%Y-%m-%d %H:%M:%S')] 배포 완료: ${DEPLOY_ENV} 환경" >> $LOG_FILE
echo "===================== 배포 완료 =====================" >> $LOG_FILE