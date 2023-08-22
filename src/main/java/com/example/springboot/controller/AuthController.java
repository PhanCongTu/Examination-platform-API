package com.example.springboot.controller;

import com.example.springboot.service.UserService;
import com.example.springboot.dto.view_model.LoginVM;
import com.example.springboot.dto.view_model.SignupVM;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;

@Validated
@RestController
@RequestMapping("")
public class AuthController {
    @Autowired
    private UserService userService;

    @PostMapping(value = "/signup", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> Signup(@Valid @RequestBody SignupVM signupVM){
        return userService.createUser(signupVM);
    }

    @PostMapping(value = "/login", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> Login(@Valid @RequestBody LoginVM loginVM){
        return userService.login(loginVM);
    }
    @GetMapping("/api/hello")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<String> sayHello(Principal principal) {
        return new ResponseEntity<>(String.format("Hello %s", principal.getName()), HttpStatus.OK);
    }
}
