package com.example.UserManagement.controllers;

import com.example.UserManagement.Dto.UserDto;
import com.example.UserManagement.service.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/profile")
public class ProfileController {
    private ProfileService profileService;

    @Autowired
    public ProfileController(ProfileService profileService){
        this.profileService=profileService;
    }

    @GetMapping("/id")
    public UserDto getProfile(@PathVariable UUID id){
        return profileService.getUserProfile(id);
    }
}
