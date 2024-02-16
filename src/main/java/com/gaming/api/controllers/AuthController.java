package com.gaming.api.controllers;

import com.gaming.api.models.AuthCredentials;
import com.gaming.business.AuthBusiness;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.naming.AuthenticationException;

@RestController
@RequestMapping("/api/v1.0/auth")
public class AuthController {

    private final AuthBusiness authBusiness;

    @Autowired
    public AuthController(AuthBusiness _authBusiness){
        authBusiness = _authBusiness;
    }

    @PostMapping
    public ResponseEntity<?> login(@RequestBody AuthCredentials credentials){
        try {
            return ResponseEntity.ok(authBusiness.login(credentials));
        } catch (AuthenticationException authenticationException){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}
