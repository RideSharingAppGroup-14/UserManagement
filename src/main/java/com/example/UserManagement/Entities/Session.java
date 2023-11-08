package com.example.UserManagement.Entities;

import com.example.UserManagement.Enums.SessionStatus;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Session extends BaseModel{
    private String token;
    @ManyToOne()
    private User user;
    private Date expiry;
    @Enumerated(EnumType.ORDINAL)
    private SessionStatus sessionStatus;

}
