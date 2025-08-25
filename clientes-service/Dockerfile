FROM eclipse-temurin:21-jre
WORKDIR /app

# Copia el JAR construido por Maven
ARG JAR_FILE=target/clientes-service-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} app.jar

EXPOSE 8081
ENTRYPOINT ["java","-jar","/app/app.jar"]
