package emil.dobrev.services.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JwtSecret {
    @Value("${spring.secret}")
    public String secret;
}