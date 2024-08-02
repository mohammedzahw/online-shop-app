package com.example.e_commerce.security;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.http.HttpServletRequest;

@Component
public class TokenUtil {
    @Autowired
    HttpServletRequest request;

    /***************************************************************************************/

    @Value("${auth.secret}")
    private String TOKEN_SECRET;

    public String generateToken(String userName, Integer userId, Integer TOKEN_VALIDITY) {

        Map<String, Object> claims = new HashMap<>();
        claims.put("userName", userName);
        claims.put("userId", userId);
        claims.put("created", new Date());

        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(generateExpirationDate(TOKEN_VALIDITY))
                .signWith(SignatureAlgorithm.HS512, TOKEN_SECRET).compact();
    }

    /***************************************************************************************/
    public String getUserName(String token) {
        if (token == null)
            return null;

        try {
            Claims claims = getClaims(token);

            return claims.get("userName").toString();
        } catch (Exception ex) {
            return null;
        }
    }

    /***************************************************************************************/
    public Integer tokenGetID(String token) {
        if (token == null)
            return null;

        try {
            Claims claims = getClaims(token);
            return (Integer) claims.get("userId");
        } catch (Exception ex) {
            return null;
        }
    }

    /***************************************************************************************/
    public Integer getUserId() {

        if (request.getHeader("Authorization") == null)
            return null;
        String token = request.getHeader("Authorization").substring("Bearer ".length());
        Claims claims = getClaims(token);

        return (Integer) claims.get("userId");
    }
    /***************************************************************************************/
    // public Customer getUser() {

    // if (request.getHeader("Authorization") == null)
    // return null;
    // String token = request.getHeader("Authorization").substring("Bearer
    // ".length());
    // Claims claims = getClaims(token);

    // return customerRepository.findById((Integer)
    // claims.get("userId")).orElse(null);

    // }
    /***************************************************************************************/

    private Date generateExpirationDate(Integer TOKEN_VALIDITY) {
        return new Date(System.currentTimeMillis() + TOKEN_VALIDITY * 1000);
    }

    /***************************************************************************************/

    public boolean isTokenValid(String token, UserDetails userDetails) {

        String username = getUserName(token);

        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    /***************************************************************************************/
    public boolean isTokenExpired(String token) {
        try {
            return getClaims(token).getExpiration().before(new Date());
        } catch (Exception ex) {
            return true;
        }

    }

    /***************************************************************************************/
    private Claims getClaims(String token) {
        Claims claims;
        try {
            claims = Jwts.parser().setSigningKey(TOKEN_SECRET)
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception ex) {
            claims = null;
        }

        return claims;
    }
}
