package emil.dobrev.services.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@RequiredArgsConstructor
@Component
public class JwtUtil {

    private final JwtSecret jwtSecret;


    public void validateToken(String token) {
        Jwts.parserBuilder().setSigningKey(jwtSecret.secret).build().parseClaimsJws(token);
    }

    public String extractUserId(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(jwtSecret.getSecretKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
