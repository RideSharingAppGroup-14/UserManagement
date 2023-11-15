package com.example.UserManagement.service;

import ch.qos.logback.core.testUtil.RandomUtil;
import com.example.UserManagement.Dto.LoginMetaData;
import com.example.UserManagement.Entities.Session;
import com.example.UserManagement.Entities.User;
import com.example.UserManagement.Enums.SessionStatus;
import com.example.UserManagement.Enums.UserType;
import com.example.UserManagement.repository.SessionRepository;
import com.example.UserManagement.repository.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.MacAlgorithm;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class AuthService {
    private UserRepository userRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    private SessionRepository sessionRepository;

    private KeyGenService keyGenService;

    @Autowired
    public AuthService(UserRepository userRepository,KeyGenService keyGenService, BCryptPasswordEncoder bCryptPasswordEncoder, SessionRepository sessionRepository){
        this.userRepository=userRepository;
        this.bCryptPasswordEncoder=bCryptPasswordEncoder;
        this.sessionRepository=sessionRepository;
        this.keyGenService=keyGenService;
    }

    public User registerUser(User user){
        if(user.getType()!= UserType.DRIVER&&user.getType()!=UserType.RIDER){
            throw new RuntimeException("invalid user type");
        }
        String password=user.getPassword();
        String encodedPassword = bCryptPasswordEncoder.encode(password);
        user.setPassword(encodedPassword);
        return userRepository.save(user);

    }

    public String login(LoginMetaData loginMetaData){
        User user=userRepository.findByEmail(loginMetaData.getEmail());
        if(user==null){
            throw new RuntimeException("User not found with given Email");
        }
        if(!bCryptPasswordEncoder.matches(loginMetaData.getPassword(),user.getPassword())){
            throw new RuntimeException("Password is Incorrect");
        }
        MacAlgorithm alg = Jwts.SIG.HS256; //or HS384 or HS256

        Map<String,Object> jsonforJWT = new HashMap<>();
        jsonforJWT.put("email",user.getEmail());
        jsonforJWT.put("id",user.getId());
        jsonforJWT.put("firstName",user.getFirstName());
        jsonforJWT.put("userType",user.getType());
        // Calculate the expiration time (24 hours from now)
        long expirationTimeInMillis = System.currentTimeMillis() + (24 * 60 * 60 * 1000); // 24 hours in milliseconds

        // Set the "exp" claim in the JWT payload
        jsonforJWT.put("exp", (expirationTimeInMillis));
        // Create the compact JWS:
        String jws = Jwts.builder().claims(jsonforJWT)
                .signWith(this.keyGenService.getSignKey(), SignatureAlgorithm.HS256).compact();
        Session session = new Session();
        session.setSessionStatus(SessionStatus.ACTIVE);
        session.setToken(jws);
        session.setUser(user);
        session.setExpiry(new Date(expirationTimeInMillis));
        sessionRepository.save(session);
        // Parse the compact JWS:
//        content = Jwts.parser().verifyWith(key).build().parseSignedContent(jws).getPayload();

        return jws;

    }

    public void logout(String token){
        Optional<Session> sessionOptional = sessionRepository.findByToken(token);
        if (sessionOptional.isEmpty()) {
            throw new RuntimeException("token invalid");
        }
        Session session = sessionOptional.get();
        session.setSessionStatus(SessionStatus.EXPIRED);
        sessionRepository.save(session);
    }



    public Session validate(String token) {
        Optional<Session> sessionOptional = sessionRepository.findByToken(token);

        if (sessionOptional.isEmpty()) {
            throw new RuntimeException("token invalid");
        }

        Session session = sessionOptional.get();

        if (!session.getSessionStatus().equals(SessionStatus.ACTIVE)) {
            throw new RuntimeException("token invalid,Session Expired");
        }


        Jws<Claims> claimsJws = Jwts.parser()
                .build()
                .parseSignedClaims(token);

        String email = (String) claimsJws.getPayload().get("email");
        Date expired = (Date) claimsJws.getPayload().get("exp");

        if (expired.before(new Date())) {
            throw new RuntimeException("token invalid,Expired");
        }


//        if (!session.)

//        return SessionStatus.ACTIVE;
        return session;
    }

}
