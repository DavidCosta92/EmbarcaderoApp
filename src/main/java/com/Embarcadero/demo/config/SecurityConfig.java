package com.Embarcadero.demo.config;

import com.Embarcadero.demo.auth.jwt.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.filters.CorsFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity (securedEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig {
    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;
    @Autowired
    private AuthenticationProvider authProvider;
    @Autowired
    private CorsConfig corsConfig;
    @Bean
    public SecurityFilterChain securityFilterChain (HttpSecurity httpSecurity) throws Exception{
        return httpSecurity
                .csrf(csrf-> csrf.disable())//.headers().disable()
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/**").permitAll()
                        // .requestMatchers("auth/restorePassword").permitAll() TODO deberia estar incluido ("/auth/**").permitAll()
                        // .requestMatchers("auth/setNewPassword").permitAll() TODO deberia estar incluido ("/auth/**").permitAll()
                        .requestMatchers("/docs/**").permitAll()
                        .requestMatchers("/api-docs/**").permitAll()
                        // .requestMatchers("/licences/**").authenticated() TODO Verificado a nivel controller con @PreAuth()
                        // .requestMatchers("/shifts/**").authenticated() TODO Verificado a nivel controller con @PreAuth()
                        // .requestMatchers("/records/**").authenticated() TODO Verificado a nivel controller con @PreAuth()
                        // .requestMatchers("/files/**").authenticated() TODO Verificado a nivel controller con @PreAuth()
                        // .requestMatchers("/person/**").authenticated() TODO Verificado a nivel controller con @PreAuth()
                        .anyRequest().authenticated() )
                .sessionManagement( sessionManagment -> sessionManagment.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authProvider)
                .addFilterBefore(jwtAuthenticationFilter , UsernamePasswordAuthenticationFilter.class)
                .build();
    }

}
