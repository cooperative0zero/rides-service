package com.modsen.software.rides.filter.tokeninfo;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class TokenInfoFilter extends OncePerRequestFilter {

    private final TokenInfoPopulator tokenInfoPopulator;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String authorizationHeader = request.getHeader("Authorization");

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String payload = authorizationHeader.substring(7);
            tokenInfoPopulator.populateTokenInfo(payload);
        }

        filterChain.doFilter(request, response);
    }
}

