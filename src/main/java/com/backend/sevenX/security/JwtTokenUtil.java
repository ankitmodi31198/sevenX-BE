package com.backend.sevenX.security;

import com.backend.sevenX.utills.Constant;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Base64;
import java.util.Date;
import java.util.function.Function;

@Component
public class JwtTokenUtil implements Serializable {

    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    public Integer getUserIdFromToken(String token) {
        String idStr = getClaimFromToken(token, Claims::getId);
        if(idStr != null) {
            try {
                return Integer.parseInt(idStr);
            }catch(Exception e){
                return null;
            }
        }
        return null;
    }

    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(Base64.getEncoder().encodeToString(Constant.JwtConst.JWT_PRIVATE_KEY.getBytes()))
                .parseClaimsJws(token)
                .getBody();
    }

    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    public String generateToken(String username, Integer id, String role) {
        return doGenerateToken(username, id, role);
        //return doGenerateDefaultToken();
    }

    private String doGenerateToken(String subject, Integer id, String role) {
        Claims claims = Jwts.claims().setSubject(subject);
        claims.put("scopes", Arrays.asList(new SimpleGrantedAuthority("ROLE_ADMIN")));
        claims.put("role", role);
        return Jwts.builder()
                .setClaims(claims)
                .setId(String.valueOf(id))
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .signWith(SignatureAlgorithm.HS256, Base64.getEncoder().encodeToString(Constant.JwtConst.JWT_PRIVATE_KEY.getBytes()))
                .setHeaderParam("typ", "JWT")
                .compact();
    }

    private String doGenerateDefaultToken() {
        Claims claims = Jwts.claims().setSubject("test@admin.com");
        claims.put("scopes", Arrays.asList(new SimpleGrantedAuthority("ROLE_ADMIN")));
        return Jwts.builder()
                .setClaims(claims)
                .setId("0")
                .setIssuer("http://e-procure.net.s3-website.ap-south-1.amazonaws.com/")
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .signWith(SignatureAlgorithm.HS256, Base64.getEncoder().encodeToString(Constant.JwtConst.JWT_PRIVATE_KEY.getBytes()))
                .setHeaderParam("typ", "JWT")
                .compact();
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = getUsernameFromToken(token);
        return (username.equals(userDetails.getUsername())
                && !isTokenExpired(token));
    }
}
