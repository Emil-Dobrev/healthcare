package emil.dobrev.services.filter;




import org.springframework.stereotype.Component;
import org.springframework.http.server.reactive.ServerHttpRequest;
import java.util.List;
import java.util.function.Predicate;

@Component
public class RouteValidator {

    public static final List<String> openApiEndPoints = List.of(
            "GET:/api/v1/doctors",
            "GET:/api/v1/doctors/{id}",
            "GET:/api/v1/patients/{id}",
            "GET:/api/v1/patients",
            "POST:/api/v1/auth/register/doctor",
            "POST:/api/v1/auth/register/patient",
            "POST:/api/v1/auth/login",
            "GET:/eureka"
    );


    public Predicate<ServerHttpRequest> isSecured =
            request -> openApiEndPoints
                    .stream()
                    .noneMatch(uri ->
                            uri.contains(request.getURI().getPath()));
//                            request.getURI().getPath().contains(uri));
}
