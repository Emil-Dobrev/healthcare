package emil.dobrev.services.shared;

import emil.dobrev.services.exception.UnauthorizedException;
import lombok.NonNull;

import static emil.dobrev.services.constant.Constants.*;

public class PermissionsUtils {

    public static void checkForPatientOrDoctorPermission(@NonNull String roles){
        if (!roles.contains(PATIENT) && !roles.contains(DOCTOR)) {
            throw new UnauthorizedException(UNAUTHORIZED);
        }
    }

    public static void checkForPatientPermission(@NonNull String roles) {
        if (!roles.contains(PATIENT)) {
            throw new UnauthorizedException(UNAUTHORIZED);
        }
    }

    public static void checkForDoctorPermission(@NonNull String roles) {
        if (!roles.contains(DOCTOR)) {
            throw new UnauthorizedException(UNAUTHORIZED);
        }
    }
}
