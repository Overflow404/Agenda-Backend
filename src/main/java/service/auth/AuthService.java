package service.auth;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.gson.Gson;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import model.User;
import java.util.Base64;
import java.util.Date;
import java.util.Map;

import static config.Configuration.JWT_KEY;
import static config.Configuration.SESSION_TIME;

public class AuthService {

    private static Base64.Decoder decoder = Base64.getUrlDecoder();
    private final String BEARER = "Bearer ";

    public boolean isAuthenticated(String jwt) {
        if (isNotAJwt(jwt)) {
            return false;
        }

        try {
            String cleanedJwt = clean(jwt);
            Map body = new Gson().fromJson(getBody(cleanedJwt), Map.class);
            long exp = parseDate(body.get("exp").toString());
            return System.currentTimeMillis() <= exp;
        } catch (Exception e) {
            return false;
        }
    }

    public ObjectNode createInfo(User user) throws JsonProcessingException {
        long currentTime = System.currentTimeMillis();

        String jwt = Jwts.builder()
                .signWith(SignatureAlgorithm.HS512, JWT_KEY)
                .setIssuedAt(new Date(currentTime))
                .setExpiration(new Date(currentTime + SESSION_TIME))
                .claim("email", user.getEmail())
                .compact();

        ObjectMapper mapper = new ObjectMapper();
        String jsonUser = mapper.writeValueAsString(user);

        JsonNodeFactory factory = JsonNodeFactory.instance;
        ObjectNode root = factory.objectNode();
        root.put("jwt", jwt);
        root.put("user", jsonUser);

        return root;
    }

    private boolean isNotAJwt(String jwt) {
        return jwt == null || jwt.isBlank() ||
                jwt.length() < BEARER.length() || !jwt.startsWith(BEARER);
    }

    private String getBody(String jwt) {
        String[] parts = jwt.split("\\.");
        return new String(decoder.decode(parts[1]));
    }

    private String clean(String jwt) {
        return jwt.substring(BEARER.length());
    }

    private long parseDate(String time) {
        return Long.valueOf(String.format("%.0f", Double.parseDouble(time))) * 1000;
    }

}
