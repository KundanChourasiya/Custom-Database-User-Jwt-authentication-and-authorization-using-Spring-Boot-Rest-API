package com.it.service.Jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.*;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.it.GlobalException.JwtException;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.Date;

@Service
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
