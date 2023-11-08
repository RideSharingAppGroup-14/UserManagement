package com.example.UserManagement.Entities;

import com.example.UserManagement.Enums.Gender;
import com.example.UserManagement.Enums.UserType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class User extends BaseModel {

    private String password;
    private String firstName;
    private String lastName;
    @Column(unique = true)
    private String email;
    private String phoneNumber;
    @Enumerated(EnumType.STRING)
    private UserType type;
    @Enumerated(EnumType.STRING)
    private Gender gender;
    private Date dob;
    @Column(unique = true)
    private String drivingLicense;
}
