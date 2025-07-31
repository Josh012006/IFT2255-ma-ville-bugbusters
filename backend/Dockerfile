FROM maven:3.9.9-eclipse-temurin-21

WORKDIR /app

COPY . .

RUN mvn clean package

# Faire une liste, les étapes de compilation, etc.
CMD ["java", "-jar", "target/maVille-3.0-SNAPSHOT-jar-with-dependencies.jar"]