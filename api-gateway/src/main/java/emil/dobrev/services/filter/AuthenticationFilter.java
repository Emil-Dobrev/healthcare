package emil.dobrev.services.filter;

import emil.dobrev.services.exception.InvalidTokenException;
import emil.dobrev.services.exception.MissingTokenException;
import emil.dobrev.services.util.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.stereotype.Component;

import static org.apache.http.HttpHeaders.AUTHORIZATION;

@Component
@Slf4j
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
                String token = exchange.getRequest().getHeaders().getFirst(AUTHORIZATION);
                if (token != null && token.startsWith(BEARER)) {
                    try {
                        token = token.substring(7);
                        //validates token
                        jwtUtil.validateToken(token);
                        String id = String.valueOf(jwtUtil.extractUserId(token));
                        var roles = jwtUtil.extractRoles(token);

                        exchange.getRequest()
                                .mutate()
                                .header("userId", id)
                                .header("roles", String.valueOf(roles));

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
