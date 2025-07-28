FROM eclipse-temurin:17

WORKDIR /app

# Copia el .jar generado por Maven
COPY . .
RUN chmod +x mvnw
RUN ./mvnw clean package -DskipTests

# Expone el puerto de Spring Boot
EXPOSE 8080

# Comando para ejecutar la aplicación
CMD ["java", "-jar", "target/demo-0.0.1-SNAPSHOT.jar"]
