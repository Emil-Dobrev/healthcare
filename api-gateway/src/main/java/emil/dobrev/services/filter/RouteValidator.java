package emil.dobrev.services.filter;




import org.springframework.stereotype.Component;
import org.springframework.http.server.reactive.ServerHttpRequest;
import java.util.List;
import java.util.function.Predicate;

@Component
public class RouteValidator {

    public static final List<String> openApiEndPoints = List.of(
            "/api/v1/doctors",
            "api/v1/auth/register/doctor",
            "api/v1/auth/register/patient",
            "api/v1/auth/login",
            "/eureka"
    );

    public Predicate<ServerHttpRequest> isSecured =
            request -> openApiEndPoints
                    .stream()
                    .noneMatch(uri -> request.getURI().getPath().contains(uri));
}
