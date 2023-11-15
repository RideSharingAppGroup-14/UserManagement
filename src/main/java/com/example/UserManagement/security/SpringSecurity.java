package com.example.UserManagement.security;

import com.example.UserManagement.Entities.Session;
import com.example.UserManagement.repository.SessionRepository;
import com.example.UserManagement.service.KeyGenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;

//
@Component
public class SpringSecurity {

    private KeyGenService keyGenService;
    private SessionRepository sessionRepository;
    @Autowired
    public SpringSecurity(KeyGenService keyGenService,SessionRepository sessionRepository){
        this.keyGenService=keyGenService;
        this.sessionRepository=sessionRepository;
    }

    @Bean
    public SecurityFilterChain filteringCriteria(HttpSecurity http) throws Exception{
        http.cors().disable();
        http.csrf().disable();
        http.authorizeHttpRequests(authorize -> authorize
                        .requestMatchers( "/auth/**").permitAll().
                anyRequest().authenticated())
                .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
        ;
//        http.authorizeHttpRequests(authorize -> authorize.requestMatchers("/auth/*").authenticated());
        return http.build();

    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter(keyGenService,sessionRepository);
    }

    @Bean
    public BCryptPasswordEncoder bcryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
