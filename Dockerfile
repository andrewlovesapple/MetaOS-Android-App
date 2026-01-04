FROM eclipse-temurin:17-jdk

WORKDIR /app

RUN apt-get update && apt-get install -y maven

COPY pom.xml .
COPY src/ src/
COPY proto/ proto/

RUN mvn clean package

ENV GRPC_SERVER_HOST=metaos-dev-container
ENV GRPC_SERVER_PORT=50051

CMD ["java", "-jar", "target/metaos-client-1.0-SNAPSHOT.jar"]