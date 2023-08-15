package emil.dobrev.services.enums;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum DoctorSpecialization {
    @JsonProperty("CARDIOLOGY")
    CARDIOLOGY,
    @JsonProperty("DERMATOLOGY")
    DERMATOLOGY,
    @JsonProperty("GASTROENTEROLOGY")
    GASTROENTEROLOGY,
    @JsonProperty("NEUROLOGY")
    NEUROLOGY,
    @JsonProperty("ONCOLOGY")
    ONCOLOGY,
    @JsonProperty("ORTHOPEDICS")
    ORTHOPEDICS,
    @JsonProperty("PEDIATRICS")
    PEDIATRICS,
    @JsonProperty("PSYCHIATRY")
    PSYCHIATRY,
    @JsonProperty("UROLOGY")
    UROLOGY,
    @JsonProperty("OPHTHALMOLOGY")
    OPHTHALMOLOGY

}
