# 1. OpenJDK 이미지를 기반으로 설정
FROM openjdk:17-jdk-slim

# 2. Spring Boot 애플리케이션의 JAR 파일을 /app 경로로 복사
COPY target/your-spring-app.jar /app/your-spring-app.jar

# 3. JAR 파일 실행 명령어
ENTRYPOINT ["java", "-jar", "/app/your-spring-app.jar"]
