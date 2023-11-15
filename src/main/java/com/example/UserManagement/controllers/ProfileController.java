package com.example.UserManagement.controllers;

import com.example.UserManagement.Dto.UserDto;
import com.example.UserManagement.service.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/profile")
public class ProfileController {
    private ProfileService profileService;

    @Autowired
    public ProfileController(ProfileService profileService){
        this.profileService=profileService;
    }

    @GetMapping("/{id}")
    public UserDto getProfile(@PathVariable UUID id){
        return profileService.getUserProfile(id);
    }
    @PutMapping("/{id}")
    public UserDto EditProfile(@PathVariable UUID id,@RequestBody UserDto user){
        return profileService.editProfile(id,user);
    }
}
