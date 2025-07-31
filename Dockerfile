FROM eclipse-temurin:24-jdk

WORKDIR /app

COPY ../.. .

CMD ["java", "-cp", "target/classes", "org.example.Main"]

