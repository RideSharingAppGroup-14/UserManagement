package com.example.UserManagement.security;

//public class JwtAuthenticationFilter {
//}

import com.example.UserManagement.Entities.Session;
import com.example.UserManagement.Enums.SessionStatus;
import com.example.UserManagement.repository.SessionRepository;
import com.example.UserManagement.service.AuthService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
//import javax.servlet.FilterChain;
//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Date;
import java.util.Optional;

public class JwtAuthenticationFilter extends OncePerRequestFilter {
//     private  SessionRepository sessionRepository;
//    @Autowired
//    public  JwtAuthenticationFilter(SessionRepository sessionRepository){
//        this.sessionRepository=sessionRepository;
//    }
//    private AuthService authService;
//    private final String jwtSecret; // Your JWT secret key
//    private final long jwtExpirationMs; // JWT expiration time in milliseconds

//    public JwtAuthenticationFilter(String jwtSecret, long jwtExpirationMs) {
//        this.jwtSecret = jwtSecret;
//        this.jwtExpirationMs = jwtExpirationMs;
//    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            String jwt = extractJwtFromRequest(request);
            if (jwt != null) {
                try {
                    Jws<Claims> claimsJws = Jwts.parser()
                            .build()
                            .parseSignedClaims(jwt);
                    String email = (String) claimsJws.getPayload().get("email");
                    Date expired = (Date) claimsJws.getPayload().get("exp");
                    if (expired.before(new Date())) {
                        throw new RuntimeException("token invalid,Expired");
                    }

                }
                catch(Exception e){
                    System.out.println(e.getMessage());
                }






            }
        } catch (ExpiredJwtException ex) {
            // Handle token expiration as needed
        }

        filterChain.doFilter(request, response);
    }

    private String extractJwtFromRequest(HttpServletRequest request) {
        // Extract the JWT token from the request (e.g., from the Authorization header)
        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            return header.substring(7);
        }
        return null;
    }
}

