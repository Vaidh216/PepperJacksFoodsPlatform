package com.pepperjacks.foodplatform.util;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Component
public class JwtUtil {
    
    @Value("${jwt.secret}")
    private String jwtSecret;
    
    @Value("${jwt.access-token-validity-ms}")
    private Long accessTokenValidity;
    
    public String generateAccessToken(Long userId, String phone, String role) {
        try {
            Instant now = Instant.now();
            Instant expiryTime = now.plus(accessTokenValidity, ChronoUnit.MILLIS);
            
            JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                    .subject(userId.toString())
                    .claim("phone", phone)
                    .claim("role", role)
                    .issueTime(Date.from(now))
                    .expirationTime(Date.from(expiryTime))
                    .build();
            
            SignedJWT signedJWT = new SignedJWT(
                    new JWSHeader(JWSAlgorithm.HS256),
                    claimsSet
            );
            
            JWSSigner signer = new MACSigner(jwtSecret.getBytes());
            signedJWT.sign(signer);
            
            return signedJWT.serialize();
        } catch (Exception e) {
            throw new RuntimeException("Error generating JWT token", e);
        }
    }
    
    public JWTClaimsSet validateAndGetClaims(String token) {
        try {
            SignedJWT signedJWT = SignedJWT.parse(token);
            JWSVerifier verifier = new MACVerifier(jwtSecret.getBytes());
            
            if (!signedJWT.verify(verifier)) {
                throw new RuntimeException("Invalid JWT signature");
            }
            
            JWTClaimsSet claims = signedJWT.getJWTClaimsSet();
            
            if (claims.getExpirationTime().before(new Date())) {
                throw new RuntimeException("JWT token has expired");
            }
            
            return claims;
        } catch (Exception e) {
            throw new RuntimeException("Error validating JWT token", e);
        }
    }
    
    public Long getUserIdFromToken(String token) {
        JWTClaimsSet claims = validateAndGetClaims(token);
        return Long.parseLong(claims.getSubject());
    }
    
    public boolean isTokenExpired(String token) {
        try {
            JWTClaimsSet claims = validateAndGetClaims(token);
            return claims.getExpirationTime().before(new Date());
        } catch (Exception e) {
            return true;
        }
    }
}

