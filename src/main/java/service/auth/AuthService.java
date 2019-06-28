package service.auth;

import com.google.gson.Gson;
import java.util.Base64;
import java.util.Map;

public class AuthService {

    private static final String NULL_BEARER = "Bearer null";
    private static Base64.Decoder decoder = Base64.getUrlDecoder();

    public boolean isAuthenticated(String jwt) {
        if (jwt == null || jwtIsNull(jwt)) {
            return false;
        }

        String clearedJwt = clean(jwt);
        Map body = new Gson().fromJson(getBody(clearedJwt), Map.class);

        long exp = parseDate(body.get("exp").toString());
        return System.currentTimeMillis() <= exp;
    }

    public String getEmail(String jwt) {
        String clearedJwt = clean(jwt);
        Map body = new Gson().fromJson(getBody(clearedJwt), Map.class);
        return body.get("email").toString();
    }

    private boolean jwtIsNull(String jwt) {
        return jwt.equals(NULL_BEARER);
    }

    private String getBody(String jwt) {
        String[] parts = jwt.split("\\.");
        return new String(decoder.decode(parts[1]));
    }

    private String clean(String jwt) {
        return jwt.substring(7);
    }

    private long parseDate(String time) {
        return Long.valueOf(String.format("%.0f", Double.parseDouble(time))) * 1000;
    }
}
