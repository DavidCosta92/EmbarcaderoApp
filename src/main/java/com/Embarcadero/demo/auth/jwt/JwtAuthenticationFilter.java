package com.Embarcadero.demo.auth.jwt;

import com.Embarcadero.demo.auth.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Enumeration;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtService jwtService;

    @Autowired
    UserDetailsService userDetailsService;

    @Autowired
    UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final String token = jwtService.getTokenFromRequest(request);
        final String username;
        if (token == null){
            filterChain.doFilter(request, response);
            return;
        } else if (token.equals("eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJkYXZpZENvc3RhIiwiaWF0IjoxNzEzNTMzNzY4LCJleHAiOjE3MTM2MjAxNjh9.8Hmj-z-cIU0GTOELnso1VAJwmKvL7aWj8pTgEEq7cRo")){
            // todo ELIMINAR EN PRODUCCION token para testing con swagger ELIMINAR EN PRODUCCION
            // todo ELIMINAR EN PRODUCCION token para testing con swagger ELIMINAR EN PRODUCCION

            username = "onlyTesting"; // todo busca user hardcodeado, con poder de "ADMIN"
            if(SecurityContextHolder.getContext().getAuthentication() == null){
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                // actualizo security contex
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
            filterChain.doFilter(request, response);
            // todo ELIMINAR EN PRODUCCION token para testing con swagger ELIMINAR EN PRODUCCION
            // todo ELIMINAR EN PRODUCCION token para testing con swagger ELIMINAR EN PRODUCCION
        } else {
            username = jwtService.getUsernameFromToken(token);
            // si es valido el token, obtengo el username y chequeo si esta authenticado en el security context
            if(username != null && SecurityContextHolder.getContext().getAuthentication() == null){
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                if(jwtService.isTokenValid(token , userDetails)){
                    // actualizo security contex
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities());
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }
            filterChain.doFilter(request, response);
        }
    }
}
