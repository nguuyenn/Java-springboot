package uth.edu.webpsy.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;
import javax.crypto.SecretKey;
import uth.edu.webpsy.models.Role;
import java.util.Date;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class JwtUtil {
    private final String SECRET_KEY = "your-very-secure-long-secret-key-32-characters";
    private final SecretKey key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    private final Set<String> blacklist = ConcurrentHashMap.newKeySet(); //danh sách đen

    // Thêm phương thức để chặn token
    public void blacklistToken(String token) {
        blacklist.add(token);
    }

    // Kiểm tra token có bị chặn không
    public boolean isTokenBlacklisted(String token) {
        return blacklist.contains(token);
    }

    //Tạo JWT
    public String generateToken(String email, Role role) {
        return Jwts.builder()
                .setSubject(email) // Định danh user bằng email
                .claim("role", role.name()) // Thêm role vào JWT
                .setIssuedAt(new Date()) // Thời gian phát hành token
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60)) // 1 giờ
                .signWith(key,SignatureAlgorithm.HS256) // Ký token với HMAC-SHA256
                .compact(); // Tạo JWT dạng chuỗi
    }

    //Trích xuất email từ JWT
    public String extractUsername(String token) {
        return Jwts.parser().verifyWith(key).build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    //Xác thực JWT
    public boolean validateToken(String token, String email) {
        if (isTokenBlacklisted(token)) {
            return false; //Token đã bị chặn
        }
        return extractUsername(token).equals(email) && !isTokenExpired(token);
    }

    //Kiểm tra token hết hạn
    private boolean isTokenExpired(String token) {
        Date expiration = Jwts.parser().verifyWith(key).build()
                .parseClaimsJws(token)
                .getBody()
                .getExpiration();
        return expiration.before(new Date());
    }
}
