package com.example.controller;

import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
public class LoginController {

    @RequestMapping("/user")
    public Principal user(Principal user){
        return user;
    }

    @GetMapping(value = "/user/current", produces = MediaType.TEXT_PLAIN_VALUE)
    public String getCurrentUsername(){
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }
}
