package emil.dobrev.services.model;

import emil.dobrev.services.enums.Country;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import static emil.dobrev.services.enums.APIParameter.*;

public class QueryParams {

    private Map<String, Object> params;

    public QueryParams() {
        params = new HashMap<>();
    }

    public QueryParams key(String key) {
        params.put(API_KEY.toString(), key);
        return this;
    }

    public QueryParams country(Country country) {
        params.put(COUNTRY.toString(), country.code());
        return this;
    }

    public QueryParams year(int year) {
        params.put(YEAR.toString(), year);
        return this;
    }

    public String queryString() {

        if (params.isEmpty()) {
            return "";
        }

        StringBuilder builder = new StringBuilder();
        Iterator<Map.Entry<String, Object>> it = params.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry e = it.next();
            builder.append(e.getKey()).append("=").append(e.getValue());
            if (it.hasNext()) {
                builder.append("&");
            }
        }

        return builder.toString();
    }

    @Override
    public String toString() {
        return queryString();
    }
}
