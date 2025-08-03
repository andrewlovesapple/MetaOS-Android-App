FROM openjdk:24-jdk-slim

WORKDIR /app

RUN apt-get update && apt-get install -y maven

COPY pom.xml .
COPY server/ server/
COPY client/ client/
COPY proto/ proto/

RUN mvn clean compile -pl server

EXPOSE 8080

CMD ["mvn", "exec:java", "-pl", "server", "-Dexec.mainClass=org.example.ServerApp"]