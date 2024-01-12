package emil.dobrev.services.shared;

import emil.dobrev.services.exception.UnauthorizedException;
import lombok.NonNull;

import static emil.dobrev.services.constant.Constants.*;

public class PermissionsUtils {

    private PermissionsUtils() {
    }

    public static void checkForUserPermissions(@NonNull String roles){
        if (!roles.contains(PATIENT) && !roles.contains(DOCTOR)) {
            throw new UnauthorizedException(UNAUTHORIZED);
        }
    }
}
