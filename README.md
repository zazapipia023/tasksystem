# Task Management System

## Technologies

* Spring Boot 3.3.4
* Spring Security
* JSON Web Tokens (JWT)
* Gradle

## Getting Started

To get started with this project, you will need to have the following installed on your local machine

* JDK 17+
* Gradle 8.11

To build and run the project, follow these steps:

* Clone the repository: `git clone https://github.com/zazapipia023/tasksystem.git`
* Navigate to project directory
* Build the project (gradle build)
* Build docker image: docker build -t task-image . (Ignore errors after build)
* Docker-compose: docker-compose up --build

-> After these steps application will be available at `http:localhost:8080`
