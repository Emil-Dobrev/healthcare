package emil.dobrev.services.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.xml.bind.DatatypeConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@RequiredArgsConstructor
@Component
public class JwtUtil {

    private final JwtSecret jwtSecret;


    public void validateToken(String token) {
        Jwts.parserBuilder()
                .setSigningKey(DatatypeConverter.parseBase64Binary(jwtSecret.secret))
                .build().parseClaimsJws(token);
    }

    public Long extractUserId(String token) {
        return extractClaim(token,
                claims ->
                        claims.get("userId", Long.class));
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(jwtSecret.secret)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
