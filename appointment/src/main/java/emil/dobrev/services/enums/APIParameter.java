package emil.dobrev.services.enums;

public enum APIParameter {
    API_KEY("key"),
    COUNTRY("country"),
    YEAR("year"),
    MONTH("month"),
    DAY("day"),
    PREVIOUS("previous"),
    UPCOMING("upcoming"),
    PUBLIC("public"),
    FORMAT("format"),
    PRETTY("pretty");

    private String param;

    APIParameter(String param) {
        this.param = param;
    }

    @Override
    public String toString() {
        return param;
    }
}