package com.example.UserManagement.service;

import com.example.UserManagement.Dto.UserDto;
import com.example.UserManagement.Entities.User;
import com.example.UserManagement.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class ProfileService {

    private UserRepository userRepository;

    @Autowired
    public ProfileService(UserRepository userRepository){
        this.userRepository=userRepository;
    }
    public UserDto getUserProfile(UUID id){
        Optional<User> user= userRepository.findById(id);

        if(user.isPresent()){
            User userObj = user.get();
            UserDto userDto = new UserDto();
            userDto.setDob(userObj.getDob());
            userDto.setEmail(userObj.getEmail());
            userDto.setDrivingLicense(userObj.getDrivingLicense());
            userDto.setGender(userObj.getGender());
            userDto.setId(userObj.getId());
            userDto.setFirstName(userObj.getFirstName());
            userDto.setLastName(userObj.getLastName());
            userDto.setPhoneNumber(userObj.getPhoneNumber());
            userDto.setType(userObj.getType());
            return userDto;
        }
        else {
            throw new RuntimeException("No User with ID:"+id+" is present");
        }
    }
}
