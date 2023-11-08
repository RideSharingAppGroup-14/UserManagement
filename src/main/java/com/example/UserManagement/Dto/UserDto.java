package com.example.UserManagement.Dto;

import com.example.UserManagement.Enums.Gender;
import com.example.UserManagement.Enums.UserType;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.UUID;

@Getter
@Setter
public class UserDto {
    private UUID id;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private UserType type;
    private Gender gender;
    private Date dob;
    private String drivingLicense;
}
