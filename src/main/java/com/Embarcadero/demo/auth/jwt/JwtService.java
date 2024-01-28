package com.Embarcadero.demo.auth.jwt;

import com.Embarcadero.demo.exceptions.customsExceptions.InvalidJwtException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
@Log4j2
public class JwtService {
    @Autowired
    private Environment environment;



    private String getSecretKey() {
        return environment.getProperty("secret.key");
    }
    public String getToken(UserDetails user) {
        return getToken(new HashMap<>(), user);
    }
    public String createTokenForRestorePassword(String email){
        return Jwts.builder()
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000*60*60*24))
                .setSubject(email)
                .signWith(getKey(), SignatureAlgorithm.HS256)
                .compact();
    }
    private String getToken(Map<String, Object> extraClaims, UserDetails user) {
        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(user.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000*60*60*24)) // este tiempo de exp, deberia importarlo desde aplication properties
                .signWith(getKey(), SignatureAlgorithm.HS256)
                .compact();
    }
    public String getTokenFromRequest(HttpServletRequest request) {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (StringUtils.hasText(authHeader) && authHeader.startsWith("Bearer ")){
            return authHeader.substring(7);
        }else{
            return null; // ==> aca debe retornar null, sino corto cadena de filtros! por eso no usar throw new InvalidJwtException("JWT not found");
        }
    }
    public String getTokenFromHeader(HttpHeaders headers) {
        final String authHeader = headers.getFirst(HttpHeaders.AUTHORIZATION);
        if (StringUtils.hasText(authHeader) && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }else{
            log.error("JWT not found");
            throw new InvalidJwtException("JWT not found");
        }
    }
    private Claims getAllClaims (String token){
        return Jwts
                .parserBuilder()
                .setSigningKey(getKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
    public <T> T getClaims (String token, Function<Claims, T> claimsResolver){
        final Claims claims = getAllClaims(token);
        return claimsResolver.apply(claims);
    }
    public String getUsernameFromToken(String token) {
        try{
            return getClaims(token , Claims:: getSubject);
        }catch (Exception e){
            log.error("Wrong JWT => "+e.getMessage());
            throw new InvalidJwtException("Wrong JWT => "+e.getMessage());
        }
    }
    private Date getExpiration(String token){
        return getClaims(token, Claims::getExpiration);
    }
    public boolean isTokenExpired(String token){
        return getExpiration(token).before(new Date());
    }
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = getUsernameFromToken(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
    private Key getKey() {
        byte[] keyBytes = Decoders.BASE64.decode(getSecretKey());
        return Keys.hmacShaKeyFor(keyBytes);
    }

}
