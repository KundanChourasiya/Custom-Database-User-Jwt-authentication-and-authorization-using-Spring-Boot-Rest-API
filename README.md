# Custom-database-User-authentication-and-authorization-using-JWT-API. 

> [!NOTE]
> ### In this Api we used Spring security, Validation and Jwt implementation for authentication and authorization and we solved all types of exception in running test cases.

## Tech Stack
- Java
- Spring Framework
- Spring Boot
- Spring Data JPA
- lombok
- Jwt
- Spring Security
- MySQL
- Postman
- Swagger UI

## Modules
* Admin Modules
* User Modules
* Open Url Modules

## Documentation
Swagger UI Documentation - http://localhost:8080/swagger-ui/

## Installation & Run
Before running the API server, you should update the database config inside the application.properties file.
Update the port number, username and password as per your local database config.
    
```
spring.datasource.url=jdbc:mysql://localhost:3306/mydb;
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.datasource.username=root
spring.datasource.password=root
```

## API Root Endpoint

```
https://localhost:8080/
http://localhost:8080/swagger-ui/
user this data for checking purpose.
```
## Step To Be Followed
> 1. Create class that will return the list of User Details.
> 2. Secure the Rest API by adding security dependecy.
> 3. Use the properties file to create custom username and password for authentication
> 4. Create the SpringSecurityConfig class to define the bean like PasswordEncoder, UserDetailsService and AuthenticationManager
> 5. Create JwtAuthenticationEntryPoint Class Which implements AuthenticationEntryPoint interface and override method commence
> 6. Create JwtHelper Class which is used to perform action like validateToken and generateToken etc
> 7. Create JWTAuthenticationFilter class which is used for the filter purpose
> 8. Create SecurityFilterConfig class to define request processing logic
> 9. Create JwtRequest and JwtResponse class
> 10. Create JwtAuthenticationController to return the JwtResponse if everything works fine



# Following pictures will help to understand flow of the Application and API's of Custom database User authentication and authorization using JWT
