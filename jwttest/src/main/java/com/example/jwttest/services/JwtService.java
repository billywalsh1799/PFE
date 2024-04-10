package com.example.jwttest.services;

import java.security.Key;
import java.util.Date;
//import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

    private static final String SECRET_KEY="404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970";
    //private static final String SECRET_KEY="3c5f0ddfcc03a6ac2a02b62e6db0501bfa142ff35e4e5b9bf73478f99b43e93e";
    //private static final String REFRESH_KEY="767ec0bd301691a4cfeb26ec59cf814059965309968ae7fab8e2321b3305d334544c0b920a3d1c14aaa1a34e09e8987e21100622f29276da1fc65f732204654d";
    private final long jwtExpiration=600000;
    private final long refreshExpiration=3600000;

    public String extractUsername(String token){
        return extractClaim(token,Claims::getSubject);
    }
    public String extractUserRole(String token) {
        return extractClaim(token, claims -> (String) claims.get("role"));
    }

    public <T> T extractClaim(String token,Function<Claims,T> claimsResolver){
        final Claims claims=extractAllClaims(token);
        return claimsResolver.apply(claims);

    }

    public String generateToken(Map<String,Object> extraClaims, UserDetails userDetails){
        return buildToken(extraClaims, userDetails,jwtExpiration);
    }

    public String generateRefreshToken(Map<String,Object> extraClaims,UserDetails userDetails){
        return buildToken(extraClaims, userDetails,refreshExpiration);
    }
    
    /* public String generateToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails);
    } */
    

    private String buildToken(Map<String,Object> extraClaims,UserDetails userDetails,long expiration){
        return Jwts.builder().setClaims(extraClaims).setSubject(userDetails.getUsername()).setHeaderParam("typ", "JWT")
        .setIssuedAt(new Date(System.currentTimeMillis()))
        .setExpiration(new Date(System.currentTimeMillis()+expiration))
        .signWith(getSignInKey(),SignatureAlgorithm.HS256)
        .compact();
    }

    public boolean isTokenValid(String token,UserDetails userDetails){
        final String username=extractUsername(token);
        final String userRole=extractUserRole(token);

        System.out.println("user role: "+userRole);

        // Check if username matches and token is not expired
        boolean isUsernameValid = username.equals(userDetails.getUsername());
        boolean isTokenExpired = !isTokenExpired(token);
        // Compare user roles
        boolean areRolesMatching = userDetails.getAuthorities().stream()
                                           .map(GrantedAuthority::getAuthority)
                                            .anyMatch(role -> role.equals(userRole));
                                            
        return isUsernameValid && isTokenExpired && areRolesMatching;
    }

    private Date extractExpiration(String token){
        return extractClaim(token, Claims::getExpiration);
    }

    private boolean isTokenExpired(String token){ 
        return extractExpiration(token).before(new Date());
    }

    private Claims extractAllClaims(String token){
        return Jwts.parserBuilder()
                    .setSigningKey(getSignInKey())
                    .build()
                    .parseClaimsJws(token).getBody();
    }
    private Key getSignInKey() {
        byte[] keyBytes=Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
