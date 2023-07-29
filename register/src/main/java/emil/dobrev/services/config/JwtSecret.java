package emil.dobrev.services.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JwtSecret {
//    @Value("${spring.secret}")
    public String secret = "kTK1B6V1E4klYEnfjSDOepThdKcb0est4LK3HZLOsao";
}