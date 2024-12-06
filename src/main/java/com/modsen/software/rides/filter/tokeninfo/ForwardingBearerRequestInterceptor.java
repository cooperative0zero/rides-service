package com.modsen.software.rides.filter.tokeninfo;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import java.util.logging.Logger;

@Component
@RequiredArgsConstructor
public class ForwardingBearerRequestInterceptor implements RequestInterceptor {

    private final UserTokenInfo userTokenInfo;

    @Override
    public void apply(RequestTemplate template) {
        final var auth = userTokenInfo.getToken();

        Logger.getGlobal().info("Forwarded token info: %s".formatted(userTokenInfo));
        template.header(HttpHeaders.AUTHORIZATION, "Bearer %s".formatted(auth));
    }
}
