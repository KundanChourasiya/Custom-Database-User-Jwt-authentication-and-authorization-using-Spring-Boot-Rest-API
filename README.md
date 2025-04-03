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
>      1. Load **_Secret key_**, **_issuer_** and **_expiry duration_**
>      2. Create PostContruct method to load the **_Jwt Algorithm_**
>      3.  Create **_generateToken_** method to generate the token.
>      4.  Create **_verifyToken**_ method to validateToken and verify User Credentials.
>    
> 5. Create **_JwtFilter_** class inside the config package.
>      1. extend the class with **_OncePerRequestFilter_**.
>      2. Inject the **_handlerExceptionResolver_** to handler filter level exception.  
>      3. create a list Array of **_Permitted_path_** which should not filter endpoint.
>      4. override **_shouldNotFilter_** method and **_doFilterInternal_** method.
>         
> 6. Create **_SecurityConfig_** class inside the Config package and create Bean **_SecurityFilterChain_** method to Authorize endpoint url with based on user role.
> 		- we can authorize url base on role in 2 ways.
>       	- Method level authorization.
>				1. Configure in *Application.class* @EnableWebSecurity and @EnableMethodSecurity(prePostEnabled = true)
> 	  			2. add Annotation in Controller methoed with @PreAuthorize("hasAuthority('ROLE_USER')")) like below.
> 	  
>       	- requestMatcher method like (.requestMatchers("/hms/api/v1/greet").hasAuthority("USER"))
>         
> 7. Create **_SwaggerConfig_** class to integrate OpenApi Components for authorize user access token.
>    
> 8. Configure **_Swagger Definition_** to use Api Documentation and all Controller Documentation.


## Important Dependency to be used
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

## Add **_Secret key_**, **_issuer_** and **_expiry duration_** in **application.properties** file.
```
# jwt configuration
jwt.secret.key=your_secret_key
jwt.issuer=apps-name
jwt.expiry.duration=86400000
```

## Create **_Jwtservice_** class inside the service package to implement
>      1. Load Secret key, issuer and expiry duration.
>      2. Create PostContruct method to load the Jwt Algorithm.
>      3.  Create generateToken method to generate the token.
>      4.  Create verifyToken method to validateToken and verify User Credentials.

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

## Create **_JwtFilter_** class inside the config package.
>      1. extend the class with OncePerRequestFilter.
>      2. Inject the handlerExceptionResolver to handler filter level exception.
>      3. create a list Array of Permitted_path which should not filter endpoint.
>      4. override shouldNotFilter method and doFilterInternal method.

```
@Component
public class JwtFilter extends OncePerRequestFilter {

    // Handle Filter level exception
    @Autowired
    @Qualifier("handlerExceptionResolver")
    private HandlerExceptionResolver exceptionResolver;

    @Autowired
    private AppUserRepository userRepository;

    @Autowired
    private JwtService jwtService;

    // list for permitted endpoints (like login/register/ open any endpoints)
    private static final List<String> PERMITTED_PATHS = List.of(
            "/api/v1/auth",
            "/api/v1/all-user-list",
            "/v3/api-docs",
            "/v2/api-docs",
            "/swagger-ui",
            "/swagger-resources",
            "/webjars"
    );

    // Skip filter for permitted endpoints (like login/register/ open any endpoints)
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return PERMITTED_PATHS.stream().anyMatch(path ->
                request.getServletPath().startsWith(path)
        );
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            String authHeader = request.getHeader("Authorization");

            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                throw new JwtException("Missing or invalid Bearer Token Authorization header");
            }

            String token = authHeader.substring(7);
            String username = jwtService.verifyToken(token);

            Optional<AppUser> user = userRepository.findByUsername(username);
            if (user.isEmpty()) {
                throw new JwtException("User not found");
            }

            AppUser appUser = user.get();
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    appUser,
                    null,
                    Collections.singleton(new SimpleGrantedAuthority(appUser.getRole()))
            );
            authentication.setDetails(new WebAuthenticationDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authentication);

            filterChain.doFilter(request, response);
        } catch (JwtException e) {
            exceptionResolver.resolveException(request, response, null, e);
        }
    }

}
```

##  Create **_SecurityConfig_** class inside the Config package and create Bean **_SecurityFilterChain_** method to Authorize endpoint url with based on user role.
> 		- we can authorize url base on role in 2 ways.
> 	  
>       	- Method level authorization.
>			1. Configure in Application.class @EnableWebSecurity and @EnableMethodSecurity(prePostEnabled = true)
> 			2. add Annotation in Controller methoed with @PreAuthorize("hasAuthority('ROLE_USER')")) like below.
> 	  
>       	- requestMatcher method like (.requestMatchers("/hms/api/v1/greet").hasAuthority("USER"))

### *SecurityConfig class* 
```
package com.it.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.AuthorizationFilter;

@Configuration
public class SecurityConfig {

    @Autowired
    private JwtFilter jwtFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.disable())
//                .authorizeHttpRequests(auth-> auth.anyRequest().permitAll())

                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/api/v1/auth/**",
                                "/v3/api-docs/**",
                                "/v2/api-docs",
                                "/swagger-ui/**",
                                "/swagger-resources/**",
                                "/webjars/**",    // Required for Swagger UI assets
                                "/api/v1/all-user-list"
                        )
                        .permitAll()
//                        .requestMatchers("/hms/api/v1/greet").hasAuthority("USER")   // hasAuthority() instead of hasRole() spring 3
                        .anyRequest()
                        .authenticated())
                .addFilterBefore(jwtFilter, AuthorizationFilter.class)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .build();
    }
}
```

### *Configure EnableWebSecurity and EnableMethodSecurity* 
```
@SpringBootApplication
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class Application {
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}
```

### *Method level authorization* 
```
@PreAuthorize("hasAuthority('ROLE_USER')")
    @GetMapping("/user")
    public ResponseEntity<String> userEndPoint(@RequestHeader(value = "Authorization", required = false) String authHeader) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String tokenVal = authHeader.substring(7);
            String username = jwtService.verifyToken(tokenVal);
            Optional<AppUser> byUsername = appUserRepository.findByUsername(username);
            if (byUsername.isPresent()) {
                AppUser appUser = byUsername.get();
                return ResponseEntity.ok("Username: " + appUser.getName() + " UserRole: " + appUser.getRole());
            }
        }
        return ResponseEntity.badRequest().body("Invalid token!");
    }
```


## Create **_SwaggerConfig_** class to integrate OpenApi Components for authorize user access token.

```
@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .addSecurityItem(new SecurityRequirement().addList("Jwt Api Access key"))
                .components(new Components().addSecuritySchemes("Jwt Api Access key", new SecurityScheme()
                        .name("Jwt Api Access key").type(SecurityScheme.Type.HTTP).scheme("bearer").bearerFormat("JWT")));

    }
}
```

## Configure **_Swagger Definition_** to use Api Documentation and all Controller Documentation.

### *Swegger Defination*
```
@SpringBootApplication
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)

// configure swagger OpenAPIDefinition
@OpenAPIDefinition(
		info = @Info(
				title = "Custom-database-User-authentication-and-authorization-using-JWT-API",
				version = "1.0",
				description = "In this Api we used Spring security, Validation and Jwt implementation for authentication and authorization and we solved all types of exception in running test cases.",
				contact = @Contact(
						name = "Kundan Kumar Chourasiya",
						email = "mailmekundanchourasiya@gmail.com"
				)
		),
		servers = @Server(
				url = "http://localhost:8080",
				description = "Custom-database-User-authentication-and-authorization-using-JWT-url"
		)
)
public class Application {
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}
```

### *All Controller  Tags,  summary and description*
```
@RestController
@RequestMapping("/api/v1")
@Tag(name = "Open endpoint url Controller", description = "To perform fetch all user/admin details without authentication and authorization")
public class OpenUrlController {

    @Autowired
    private AppUserService service;

    // url: http://localhost:8080/api/v1/all-user-list
    @Operation(
            summary = "Get operation for fetch the User/Admin both Details",
            description = "It is used to fetch the all user details with authentication and authorization this endpoint is open for all user."
    )
    @GetMapping("all-user-list")
    public ResponseEntity<List<AppUserDto>> getUserList(){
        List<AppUserDto> allUsers = service.getAllUsers();
        return new ResponseEntity<>(allUsers, HttpStatus.FOUND);
    }
}
```

### Following pictures will help to understand flow of API

### *Swagger*

![image](https://github.com/user-attachments/assets/3cbb539a-fca6-4d28-a3b7-d0cf05c238eb)


### *PostMan*

Url - http://localhost:8080/api/v1/auth/user/signup
![image](https://github.com/user-attachments/assets/82e8b7a6-ab4c-411f-aac0-8fec11184121)

Url - http://localhost:8080/api/v1/auth/admin/signup
![image](https://github.com/user-attachments/assets/4bd4fa20-2aeb-4952-8d99-84b24331e6c6)

Url - http://localhost:8080/api/v1/auth/login
![image](https://github.com/user-attachments/assets/81684301-c71e-41d3-a3f5-765f0ecc88c3)

Url - http://localhost:8080/api/v1/user
![image](https://github.com/user-attachments/assets/3b85f930-2148-4d36-911f-b9582bb4d73a)

Url - http://localhost:8080/api/v1/admin
![image](https://github.com/user-attachments/assets/b8bec602-fd3a-4861-8421-40218d90045a)

Url - http://localhost:8080/api/v1/greet
![image](https://github.com/user-attachments/assets/f1a0ab51-cd86-421c-abd4-0c45a4e82026)

Url - http://localhost:8080/api/v1/login/user/details
![image](https://github.com/user-attachments/assets/42548514-0286-40b0-a860-29a441f13a65)

Url - http://localhost:8080/api/v1/all-user-list
![image](https://github.com/user-attachments/assets/4fe8d495-3640-413c-8af6-ddbde3620864)

