package com.modsen.software.rides.filter.tokeninfo;

import com.modsen.software.rides.filter.tokeninfo.util.JwtParser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

@Component
@RequiredArgsConstructor
public class TokenInfoPopulator {

    private final UserTokenInfo userTokenInfo;

    public void populateTokenInfo(String payload) {
        Map<String, Object> claims = JwtParser.parseJwtPayload(payload);

        userTokenInfo.setToken(payload);
        userTokenInfo.setId(Integer.toUnsignedLong((Integer) claims.get("userId")));
        userTokenInfo.setRoles(parseRealmRoles(claims));

        Logger.getGlobal().info("Received token info: %s".formatted(userTokenInfo));
    }

    public List<String> parseRealmRoles(Map<String, Object> claims) {
        Map<String, Object> realmAccess = (Map<String, Object>) claims.get("realm_access");
        if (realmAccess != null && realmAccess.containsKey("roles")) {
            return ((List<String>) realmAccess.get("roles"));
        }
        return List.of();
    }
}
