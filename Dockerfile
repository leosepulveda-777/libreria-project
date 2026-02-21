# 1. Etapa de construcción
FROM maven:3.9.9-eclipse-temurin-21 AS builder
WORKDIR /app

# Copiamos solo el archivo de configuración de dependencias
COPY pom.xml .

# COMENTADO O ELIMINADO: Ya no necesitamos copiar mvnw ni .mvn
# COPY mvnw .
# COPY .mvn .mvn
# RUN chmod +x mvnw

# Copiamos el código fuente
COPY src ./src

# CAMBIO CRÍTICO: Usamos 'mvn' en lugar de './mvnw'
RUN mvn package -DskipTests

# 2. Etapa de ejecución
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Copiamos el JAR generado en la etapa anterior
COPY --from=builder /app/target/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java","-jar","app.jar"]
