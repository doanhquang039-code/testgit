# Stage 1: Build
FROM eclipse-temurin:21-jdk-jammy AS build

WORKDIR /workspace

COPY .mvn/ .mvn/
COPY mvnw mvnw
COPY mvnw.cmd mvnw.cmd
COPY pom.xml pom.xml

RUN chmod +x mvnw

RUN ./mvnw -q -DskipTests dependency:go-offline

COPY src/ src/
COPY public/ public/

RUN ./mvnw -q -DskipTests package


# Stage 2: Run
FROM eclipse-temurin:21-jre-jammy

WORKDIR /app

RUN apt-get update -o Acquire::Retries=3 \
  && apt-get install -y --no-install-recommends curl \
  && rm -rf /var/lib/apt/lists/* \
  && useradd -r -u 10001 -g root appuser \
  && mkdir -p /app/public/test1 \
  && chown -R appuser:root /app

COPY --from=build /workspace/target/*.jar /app/app.jar

EXPOSE 8080

ENV JAVA_OPTS=""

HEALTHCHECK --interval=10s --timeout=3s --retries=20 \
  CMD curl -fsS http://localhost:8080/actuator/health || exit 1

USER appuser

ENTRYPOINT ["sh","-c","java $JAVA_OPTS -jar /app/app.jar"]