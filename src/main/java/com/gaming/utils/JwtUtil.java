package com.gaming.utils;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;


public class JwtUtil {
    private static final String SECRET_KEY = "c2VjcmV0LWdhbWluZy1zb2NpYWwtbWVkaWE=";
    private static final long EXPIRATION_TIME = 3_600_000;

    public static String getAuthToken(String username){
        Date now = new Date();
        Date expiration = new Date(now.getTime() + EXPIRATION_TIME);

        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(now)
                .setExpiration(expiration)
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }
}
