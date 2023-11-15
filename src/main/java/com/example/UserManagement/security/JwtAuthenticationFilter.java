package com.example.UserManagement.security;

//public class JwtAuthenticationFilter {
//}

import com.example.UserManagement.Entities.Session;
import com.example.UserManagement.Enums.SessionStatus;
import com.example.UserManagement.repository.SessionRepository;
import com.example.UserManagement.service.AuthService;
import com.example.UserManagement.service.KeyGenService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.security.Key;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private KeyGenService keyGenService;
    private SessionRepository sessionRepository;

    @Autowired
    public JwtAuthenticationFilter(KeyGenService keyGenService,SessionRepository sessionRepository){
        this.keyGenService=keyGenService;
        this.sessionRepository=sessionRepository;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String path = request.getRequestURI();
        if (!path.startsWith("/auth/")) {
            try {
                String jwt = extractJwtFromRequest(request);
                Optional<Session> sessionOptional = sessionRepository.findByToken(jwt);
                if (sessionOptional.isPresent()) {
                    Session session = sessionOptional.get();
                    if (session.getSessionStatus() == SessionStatus.EXPIRED) {
                        throw new RuntimeException("Invalid Token,session marked expired in our System.");
                    }
                }
                if (jwt != null) {

                        Jws<Claims> claimsJws = Jwts.parser()
                                .setSigningKey(this.keyGenService.getSignKey())
                                .build()
                                .parseSignedClaims(jwt);
                        String email = (String) claimsJws.getPayload().get("email");
                        Long expiryInSeconds = (Long) claimsJws.getPayload().get("exp");
                        Date expired = (new Date(expiryInSeconds));
                        if (expired.before(new Date())) {
                            throw new RuntimeException("Token Expired");
                        }
                        List<GrantedAuthority> authorities = Arrays.asList(new SimpleGrantedAuthority("ROLE_USER"));
                        // Example: Set up authentication
                        Authentication authentication = new UsernamePasswordAuthenticationToken(email, null, authorities);
                        SecurityContextHolder.getContext().setAuthentication(authentication);



                }
            } catch (RuntimeException ex) {
                // Handle token expiration as needed
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                response.getWriter().write("Error processing request: " + ex.getMessage());
                return;
            }
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

