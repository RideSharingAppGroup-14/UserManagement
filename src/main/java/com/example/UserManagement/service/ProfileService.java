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

    public UserDto editProfile(UUID id, UserDto userDto){
        Optional<User> user= userRepository.findById(id);
        if(!user.isPresent()){
            throw new RuntimeException("No user with id:"+id+" exists");
        }
        else{
            if(!userDto.getEmail().equals(user.get().getEmail())||!userDto.getDrivingLicense().equals(user.get().getDrivingLicense())){
                System.out.println(userDto.getEmail()!=user.get().getEmail());
                System.out.println(userDto.getDrivingLicense()!=user.get().getDrivingLicense());
                throw new RuntimeException("Cannot Edit Email and Driving License fields.");
            }
            User userToBeSaved = new User();
            userToBeSaved.setPassword(user.get().getPassword());
            userToBeSaved.setDob(userDto.getDob());
            userToBeSaved.setEmail(userDto.getEmail());
            userToBeSaved.setGender(userDto.getGender());
            userToBeSaved.setDrivingLicense(userDto.getDrivingLicense());
            userToBeSaved.setFirstName(userDto.getFirstName());
            userToBeSaved.setLastName(userDto.getLastName());
            userToBeSaved.setType(userDto.getType());
            userToBeSaved.setId(user.get().getId());
            User savedUser=userRepository.save(userToBeSaved);
            UserDto responseUserDto=new UserDto();
            responseUserDto.setType(savedUser.getType());
            responseUserDto.setLastName(savedUser.getLastName());
            responseUserDto.setPhoneNumber(savedUser.getPhoneNumber());
            responseUserDto.setFirstName(savedUser.getFirstName());
            responseUserDto.setGender(savedUser.getGender());
            responseUserDto.setId(savedUser.getId());
            responseUserDto.setEmail(savedUser.getEmail());
            responseUserDto.setDob(savedUser.getDob());
            responseUserDto.setDrivingLicense(savedUser.getDrivingLicense());
            return responseUserDto;
        }
    }
}
