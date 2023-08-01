package emil.dobrev.services.util;

import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class JwtUtil {

    private final JwtSecret jwtSecret;

    public void validateToken(final String token) {
        Jwts.parser().setSigningKey(jwtSecret.secret).parseClaimsJws(token);
    }
}
