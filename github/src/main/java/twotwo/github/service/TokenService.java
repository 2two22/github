import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.stereotype.Service;

@Service
public class TokenService {

    @Value("${spring.jwt.secret}")
    private String secretKey; // 이 키는 안전하게 저장해야 합니다.

    public Long getId(String token) {
        return this.parseClaims(token).get("id", Long.class);
    }

    private Claims parseClaims(String token) {
        return Jwts.parser()
                .setSigningKey(secretKey.getBytes())
                .parseClaimsJws(token)
                .getBody();
    }
}