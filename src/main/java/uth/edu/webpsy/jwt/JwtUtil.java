package uth.edu.webpsy.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;
import javax.crypto.SecretKey;
import uth.edu.webpsy.models.Role;
import java.util.Date;

@Component
public class JwtUtil {
    private final String SECRET_KEY = "your-very-secure-long-secret-key-32-characters";
    private final SecretKey key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());

    // Tạo JWT
    public String generateToken(String email, Role role) {
        return Jwts.builder()
                .setSubject(email)
                .claim("role", role.name())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60)) // 1h
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    // Trích xuất email
    public String extractUsername(String token) {
        return Jwts.parser().verifyWith(key).build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    // Xác thực JWT
    public boolean validateToken(String token, String email) {
        return extractUsername(token).equals(email) && !isTokenExpired(token);
    }

    // Kiểm tra hết hạn
    private boolean isTokenExpired(String token) {
        Date expiration = Jwts.parser().verifyWith(key).build()
                .parseClaimsJws(token)
                .getBody()
                .getExpiration();
        return expiration.before(new Date());
    }
}
