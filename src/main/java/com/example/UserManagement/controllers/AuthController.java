package com.example.UserManagement.controllers;

import com.example.UserManagement.Dto.LoginMetaData;
import com.example.UserManagement.Dto.LogoutMetaData;
import com.example.UserManagement.Entities.User;
import com.example.UserManagement.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private AuthService authService;
    @Autowired
    public AuthController(AuthService authService){
        this.authService=authService;
    }
    @PostMapping("/signup")
    public void Register(@RequestBody User user){
        authService.registerUser(user);
    }

    @PostMapping("/login")
    public String login(@RequestBody LoginMetaData loginMetaData){
        return authService.login(loginMetaData);
    }

    @PostMapping("/logout")
    public void logout(@RequestBody LogoutMetaData logoutMetaData){
        authService.logout(logoutMetaData.getToken());
    }
}
