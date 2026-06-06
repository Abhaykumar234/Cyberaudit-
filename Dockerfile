# ── Stage 1: Build ────────────────────────────────────────────────────────────
FROM eclipse-temurin:21-jdk-jammy AS build

WORKDIR /app

# Install Maven (no mvnw needed)
RUN apt-get update && \
    apt-get install -y --no-install-recommends maven && \
    rm -rf /var/lib/apt/lists/*

# Cache dependency layer (copy pom first, download deps, then copy source)
COPY pom.xml .
RUN mvn dependency:go-offline -q

COPY src ./src
RUN mvn clean package -DskipTests -q

# ── Stage 2: Run ──────────────────────────────────────────────────────────────
FROM eclipse-temurin:21-jre-jammy

WORKDIR /app

COPY --from=build /app/target/cyberaudit-pro-*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]