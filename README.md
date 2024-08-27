# Spring Boot Security with JWT Implementation

lang [EN]: [en](https://github.com/fpetranzan/java-spring-boot-security/blob/master/README.md) | [it](https://github.com/fpetranzan/java-spring-boot-security/blob/master/README_it.md)

This project is a practical implementation of security using Spring Boot and JSON Web Tokens (JWT).
A MySQL database is used for data management.

## What has been implemented
* User registration
* Email verify
* User login
* Forgot Password
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

* Clone the repository: `https://github.com/fpetranzan/java-spring-boot-security`
* Navigate to the project directory: cd spring-boot-security-jwt
* Add database "jwt_security" to MySQL
* Build the project: mvn clean install
* Run the project: mvn spring-boot:run

-> The application will be available at http://localhost:8088.

## Testing the project

### Test email verification
To be able to test sending emails on your local environment, you can use the following open-source project:
https://github.com/gessnerfl/fake-smtp-server

After downloading the `.jar` file, move it to a folder of your choice.
Open a terminal in the folder where the .jar file is present and run the command:

```
java -jar fake-smtp-server-<version>.jar
```

Open a new window in your browser and go to the path: `http://localhost:8080/`

From this path it will be possible to see the emails that will be sent

### Test project endpoints
A postman collection has been created with all calls already defined.
This way you can test all the created endpoints.
You can retrieve the collection and import it to your Postman from here:

[java-spring-boot-security - Postman Collection](https://github.com/fpetranzan/spring-boot-jwt-security/blob/master/src/main/resources/spring-boot-jwt-security.postman_collection.json)

