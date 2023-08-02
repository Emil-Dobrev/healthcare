package emil.dobrev.services.filter;

import emil.dobrev.services.exception.InvalidTokenException;
import emil.dobrev.services.exception.MissingTokenException;
import emil.dobrev.services.util.JwtUtil;
import io.jsonwebtoken.Claims;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.stereotype.Component;

import static org.apache.http.HttpHeaders.AUTHORIZATION;

@Component
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {

    private static final String BEARER = "Bearer ";
    private final RouteValidator routeValidator;
    private final JwtUtil jwtUtil;


    AuthenticationFilter(RouteValidator routeValidator, JwtUtil jwtUtil) {
        super(Config.class);
        this.routeValidator = routeValidator;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public GatewayFilter apply(Config config) {
        return ((exchange, chain) -> {
            if (routeValidator.isSecured.test(exchange.getRequest())) {
                //header contains token or not
                if (!exchange.getRequest().getHeaders().containsKey(AUTHORIZATION)) {
                    throw new MissingTokenException("Missing token");
                }
                String token = exchange.getRequest().getHeaders().get(AUTHORIZATION).get(0);
                if (token != null && token.startsWith(BEARER)) {
                    try {
                        jwtUtil.validateToken(token);
                        exchange.getRequest()
                                .mutate()
                                .header("userId", jwtUtil.extractUserId(token))
                                .header("Roles", jwtUtil.extractClaim(token, Claims::getAudience));
                    } catch (Exception exception) {
                        throw new InvalidTokenException("Invalid token");
                    }
                }
            }
            return chain.filter(exchange);
        });
    }

    public static class Config {

    }
}
