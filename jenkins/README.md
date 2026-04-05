# 1. Jenkins 컨테이너 실행

cd jenkins
docker compose up -d

# 2. 초기 비밀번호 확인

docker exec jenkins cat /var/jenkins_home/secrets/initialAdminPassword

이후 http://localhost:8080 접속 → 초기 설정 → New Item → Pipeline 생성 → Pipeline 섹션에서
Pipeline script from SCM or Pipeline script로 Jenkinsfile 등록하면 됩니다.
