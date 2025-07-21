FROM eclipse-temurin:17

WORKDIR /app

# Copia el .jar generado por Maven
COPY target/demo-0.0.1-SNAPSHOT.jar app.jar

# Expone el puerto de Spring Boot
EXPOSE 8080

# Comando para ejecutar la aplicación
ENTRYPOINT ["java", "-jar", "app.jar"]
