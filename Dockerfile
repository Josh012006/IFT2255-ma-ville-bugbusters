FROM maven:3.9.9-eclipse-temurin-21

WORKDIR /app

COPY . . 

//Si on a des dépendances ajouter ici

RUN mvn clean package

//Faire une liste, les étapes de compilation, etc.
CMD ["java", "-jar", "target/maville-3.0-SNAPSHOT.jar"]