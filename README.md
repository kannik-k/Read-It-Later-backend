# Read It Later

## Overview

This is a backend application built with Java and Gradle. This README will guide you through the steps to build, run, and Dockerize the application.

## Prerequisites

Before you begin, ensure you have met the following requirements:

- Java JDK 21 installed
- Gradle installed (optional, as it's included in the project)
- Docker installed

## How to Build and Run the Application

1. Clone the repository:
2. Run the application using Gradle:
```
./gradlew clean build
java -jar .\build\libs\iti0302-2024-backend-0.0.1-SNAPSHOT.jar
```
3. Access the application at http://localhost:8080

## Creating and Running a Docker Container

1. Create a docker-compose.yml file in the root of your project with the following content:
```
version: '3.8'

services:
  web-project:
    image: yourusername/your-image-name:tag
    ports:
      - "8080:8080"
    restart: always

```
Replace yourusername/your-image-name:tag with your Docker Hub username and the desired image name and tag.

2. Run the application using Docker Compose:
```
docker-compose up -d
```


