FROM openjdk:17
EXPOSE 8080

COPY build.gradle settings.gradle ./
COPY gradlew ./
COPY gradle ./gradle/
COPY src ./src

RUN chmod +x ./gradlew

COPY build/libs/*.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]