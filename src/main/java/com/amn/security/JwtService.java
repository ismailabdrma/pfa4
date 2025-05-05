package com.amn.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    @Value("${application.security.jwt.secret-key}")
    private String secretKey;

    @Value("${application.security.jwt.expiration}")
    private long jwtExpiration; // milliseconds

    // ✅ Get signing key (Base64-decoded secret)
    private SecretKey getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes); // Must be at least 256 bits
    }

    // ✅ Extract username from token (subject)
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // ✅ Generic claim extraction
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    // ✅ Parse all claims using parserBuilder() (JJWT 0.12.x)
    private Claims extractAllClaims(String token) {
        JwtParser parser = Jwts.parser().verifyWith(getSignInKey()).build();
        return parser.parseSignedClaims(token).getPayload();
    }

    // ✅ Generate token with only username
    public String generateToken(String username) {
        return generateToken(Map.of(), username);
    }

    // ✅ Generate token with extra claims (e.g., role)
    public String generateToken(Map<String, Object> extraClaims, String username) {
        if (extraClaims.containsKey("role")) {
            String role = (String) extraClaims.get("role");
            extraClaims.put("role", role);  // ✅ simple string
            // Prefix for Spring Security
        }

        return Jwts.builder()
                .claims(extraClaims)
                .subject(username)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + jwtExpiration))
                .signWith(getSignInKey(), Jwts.SIG.HS256) // ✅ updated for 0.12.x
                .compact();
    }

    // ✅ Validate token by username and expiration
    public boolean isTokenValid(String token, String username) {
        final String extractedUsername = extractUsername(token);
        return extractedUsername.equals(username) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }
}
