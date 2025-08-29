# Step 1: Use a lightweight JDK image
FROM eclipse-temurin:17-jdk-alpine

# Step 2: Set working directory
WORKDIR /app

# Step 3: Copy jar file
COPY target/myapp-0.0.1-SNAPSHOT.jar app.jar

# Step 4: Expose port
EXPOSE 8080

# Step 5: Run the jar
ENTRYPOINT ["java","-jar","app.jar"]
