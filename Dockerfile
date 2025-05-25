FROM gradle:8.5-jdk17 AS build
ENV GRADLE_OPTS="-Dorg.gradle.daemon=false -Djava.net.preferIPv4Stack=true"
ENV JAVA_OPTS="-Djava.net.preferIPv4Stack=true"
WORKDIR /app
COPY . .
RUN gradle :service:bootJar -x test --no-daemon

FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=build /app/service/build/libs/*.jar app.jar
ENV TZ=Asia/Seoul
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]