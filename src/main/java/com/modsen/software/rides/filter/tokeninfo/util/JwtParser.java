package com.modsen.software.rides.filter.tokeninfo.util;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Base64;
import java.util.Map;

public class JwtParser {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    public static Map<String, Object> parseJwtPayload(String token) {
        try {
            String[] parts = token.split("\\.");
            if (parts.length < 2) {
                throw new IllegalArgumentException("Invalid JWT format");
            }

            String payload = new String(Base64.getUrlDecoder().decode(parts[1]));
            return OBJECT_MAPPER.readValue(payload, Map.class);
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse JWT", e);
        }
    }
}
