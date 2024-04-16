# Spring Boot 3.0 Security with JWT Implementation
This project is a practical implementation of security using Spring Boot and JSON Web Tokens (JWT).
A MySQL database is used for data management.

## What has been implemented
* User registration
* User login
* Two-factor authentication
* Refresh token
* Change password
* Logout mechanism

### Implementation details
* JWT authentication
* Password encryption using BCrypt
* Role-based authorization with Spring Security
* Customized access denied handling

## Technologies
* Spring Boot 3.2.4
* Spring Security
* JSON Web Tokens (JWT)
* BCrypt
* Maven
* MySQL

## Getting Started
To get started with this project, you will need to have the following installed on your local machine:

* JDK 17+
* Maven 3+


To build and run the project, follow these steps:

* Clone the repository: `https://github.com/fpetranzan/spring-boot-jwt-security.git`
* Navigate to the project directory: cd spring-boot-security-jwt
* Add database "jwt_security" to mysql
* Build the project: mvn clean install
* Run the project: mvn spring-boot:run

-> The application will be available at http://localhost:8080.

## Testing the project
A postman collection has been created with all calls already defined.
This way you can test all the created endpoints.
You can retrieve the collection and import it to your Postman from here:

[spring-boot-jwt-security - Postman Collection](https://github.com/fpetranzan/spring-boot-jwt-security/blob/master/src/main/resources/spring-boot-jwt-security.postman_collection.json)