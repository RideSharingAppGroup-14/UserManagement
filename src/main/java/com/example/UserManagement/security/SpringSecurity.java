package com.example.UserManagement.security;

import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;
//
@Component
public class SpringSecurity {

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
        return new JwtAuthenticationFilter();
    }

    @Bean
    public BCryptPasswordEncoder bcryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
