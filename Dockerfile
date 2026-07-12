FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean javafx:jlink

FROM ubuntu:22.04
RUN apt-get update && apt-get install -y \
    libgtk-3-0 libx11-6 libxext6 libxrender1 libxtst6 libxi6 \
    libxxf86vm1 libxrandr2 libasound2 \
    && rm -rf /var/lib/apt/lists/*
WORKDIR /app
COPY --from=build /app/target/app ./app
CMD ["./app/bin/app"]