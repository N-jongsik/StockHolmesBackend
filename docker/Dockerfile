FROM openjdk:17-jdk-slim

# Docker 설치를 위한 패키지 추가
RUN apt-get update && apt-get install -y \
    apt-transport-https \
    ca-certificates \
    curl \
    gnupg \
    lsb-release \
    sudo \
    iputils-ping \
    net-tools \
    dnsutils \
    && curl -fsSL https://download.docker.com/linux/debian/gpg | tee /etc/apt/trusted.gpg.d/docker.asc \
    && echo "deb [arch=amd64] https://download.docker.com/linux/debian $(lsb_release -cs) stable" | tee /etc/apt/sources.list.d/docker.list \
    && apt-get update \
    && apt install -y docker-ce-cli docker-compose-plugin containerd.io \
    && apt-get clean \
    && rm -rf /var/lib/apt/lists/*

# Docker 소켓 마운트 설정
VOLUME /var/run/docker.sock

# 사용자 추가 & 권한 설정
RUN groupadd -g 998 docker \
    && useradd -u 1000 -g docker -m jenkins \
    && usermod -aG docker jenkins \
    && usermod -aG sudo jenkins \
    && echo "jenkins ALL=(ALL) NOPASSWD: ALL" >> /etc/sudoers

# Docker 명령어 실행을 위한 유저 설정
USER jenkins

# 작업 디렉토리 설정
WORKDIR /app

# JAR 파일 복사
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} app.jar

# 실행 명령
ENTRYPOINT ["java", "-jar", "app.jar"]