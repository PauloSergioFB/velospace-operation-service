package br.com.fiap.javaadv.VeloSpace.infrastructure.security;

import java.io.IOException;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class InternalApiKeyFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws IOException, ServletException {

        String path = request.getRequestURI();

        if (!path.startsWith("/internal/")) {
            filterChain.doFilter(request, response);
            return;
        }

        String receivedApiKey = request.getHeader("X-Internal-Api-Key");

        if (!"${internal.api-key}".equals(receivedApiKey)) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.setContentType("application/json");
            response.getWriter().write("""
                    {"message":"Acesso interno não autorizado"}
                    """);
            return;
        }

        filterChain.doFilter(request, response);
    }

}
