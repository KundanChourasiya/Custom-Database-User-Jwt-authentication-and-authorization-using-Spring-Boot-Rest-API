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
> 2. Secure the Rest API by adding security dependecy and adding Jwt dependency.
>    
> 3. Add **_Secret key_**, **_issuer_** and **_expiry duration_** in **application.properties** file.
>    
> 4. Create **_Jwtservice_** class inside the service package to implement
>      1. **_Secret key_**, **_issuer_** and **_expiry duration_**
>      2. Create PostContruct method to load the **_Jwt Algorithm_**
>      3.  Create **_generateToken_** method to generate the token.
>      4.  Create **_verifyToken**_ method to validateToken and verify User Credentials.
>    
> 5. Create **_JwtFilter_** class inside the config package.
>      1. extend the class with **_OncePerRequestFilter_**.
>      2. Inject the **_handlerExceptionResolver_** dependency to handler filter level exception.
>      3. create a list Array of **_Permitted_path_** which should not filter endpoint.
>      4. override **_shouldNotFilter_** method and **_doFilterInternal_** method.
>         
> 6. Create **_SecurityConfig_** class inside the Config package and create Bean **_SecurityFilterChain_** method to Authorize endpoint url with based on user role.
> 
> 7. Create **_SwaggerConfig_** class to integrate OpenApi Components for authorize user access token.

### important Dependency to be used
1. For rest api
``` 
 <dependency>
     <groupId>org.springframework.boot</groupId>
     <artifactId>spring-boot-starter-web</artifactId>
 </dependency>
```

2. For Getter and Setter
``` 
 <dependency>
     <groupId>org.projectlombok</groupId>
     <artifactId>lombok</artifactId>
     <optional>true</optional>
 </dependency>
```

3. For Security
``` 
 <dependency>
     <groupId>org.springframework.boot</groupId>
     <artifactId>spring-boot-starter-security</artifactId>
 </dependency>
```

4. For JWT
``` 
 <!-- https://mvnrepository.com/artifact/com.auth0/java-jwt -->
<dependency>
	<groupId>com.auth0</groupId>
	<artifactId>java-jwt</artifactId>
	<version>4.4.0</version>
</dependency>
```

5. For Swagger
``` 
<dependency>
	<groupId>org.springdoc</groupId>
	<artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
	<version>2.3.0</version> <!-- Latest version -->
</dependency>
```

## Add Secret key, issuer and expiry duration in application.properties file.
```
# jwt configuration
jwt.secret.key=your_secret_key
jwt.issuer=apps-name
jwt.expiry.duration=86400000
```

## Create Jwtservice class inside the service package to implement
	- Secret key, issuer and expiry duration
 	- Create PostContruct method to load the Jwt Algorithm
	- Create generateToken method to generate the token.
	- Create _verifyToken_ method to validateToken and verify User Credentials.
```@Service
public class JwtService {

    @Value("${jwt.secret.key}")
    private String secretKey;

    @Value("${jwt.issuer}")
    private String issuer;

    @Value("${jwt.expiry.duration}")
    private String expiryTime; // time in milliseconds

    private Algorithm algorithm;

    @PostConstruct
    public void postConstruct() throws UnsupportedEncodingException {
        algorithm = Algorithm.HMAC256(secretKey);
    }

    // generate token
    public String generateToken(String username) {
        try {
            long expiryTimeMilli = Long.parseLong(expiryTime);
            return JWT.create()
                    .withClaim("name", username)
                    .withExpiresAt(new Date(System.currentTimeMillis() + expiryTimeMilli))
                    .withIssuer(issuer)
                    .sign(algorithm);
        } catch (Exception e) {
            throw new JwtException("Error creating JWT token: " + e.getMessage());
        }

    }

    // Verify Token with Exception Handling
    public String verifyToken(String token)  {
        try {
            DecodedJWT decodedJWT = JWT.require(algorithm)
                    .withIssuer(issuer)
                    .build()
                    .verify(token);
            return decodedJWT.getClaim("name").asString();
        } catch (SignatureVerificationException e) {
            throw new JwtException("Invalid JWT signature");
        } catch (TokenExpiredException e) {
            throw new JwtException("JWT token has expired");
        } catch (AlgorithmMismatchException e) {
            throw new JwtException("JWT algorithm mismatch");
        } catch (JWTDecodeException e) {
            throw new JwtException("Invalid JWT token format");
        } catch (JWTVerificationException e) {
            throw new JwtException("JWT verification failed");
        }
    }
}
```
