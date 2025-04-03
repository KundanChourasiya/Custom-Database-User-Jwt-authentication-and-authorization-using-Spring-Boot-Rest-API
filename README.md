# Custom-database-User-authentication-and-authorization-using-JWT-API. 

> [!NOTE]
> ### In this Api we used Spring Security, Validation and Jwt implementation for authentication and authorization and we solved all types of exception in running test cases.

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
> 1. Create Rest Api will return to user authentication with database custom user details.
>    
>    **Project Documentation**
>    - **Entity** - AppUser (class)
>    - **Payload** - AppUserDto, ApiResponceDto, ErrorDto, LoginDto, TokenDto (class)
>    - **Repository** - AppUserRepository (interface)
>    - **Service** - AppUserService (interface), AppUserServiceImpl, JwtService (class)
>    - **Controller** - AuthUserController, UserAccessController, OpenUrlController (Class)
>    - **Global Exception** - GlobalException, JwtException (class)
>    - **Config** - SecurityConfig, JwtFilter, SwaggerConfig (Class)
>      
> 3. Secure the Rest API by adding security dependecy and adding Jwt dependency.
>    
> 5. Add **_Secret key_**, **_issuer_** and **_expiry duration_** in pom.xml file.
>    
> 6. Create **Jwtservice** class inside the service package to implement
>      1. **_Secret key_**, **_issuer_** and **_expiry duration_**
>      2. Create PostContruct method to load the **_Jwt Algorithm_**
>      3.  Create **_generateToken_** method to generate the token.
>      4.  Create **_verifyToken**_ method to verify token User Credentials.
>    
> 7. Create **__JwtFilter** class inside the config package.
>      1.  
> 9. Create JwtAuthenticationEntryPoint Class Which implements AuthenticationEntryPoint interface and override method commence
> 10. Create JwtHelper Class which is used to perform action like validateToken and generateToken etc
> 11. Create JWTAuthenticationFilter class which is used for the filter purpose
> 12. Create SecurityFilterConfig class to define request processing logic
> 13. Create JwtRequest and JwtResponse class
> 14. Create JwtAuthenticationController to return the JwtResponse if everything works fine



# Following pictures will help to understand flow of the Application and API's of Custom database User authentication and authorization using JWT
