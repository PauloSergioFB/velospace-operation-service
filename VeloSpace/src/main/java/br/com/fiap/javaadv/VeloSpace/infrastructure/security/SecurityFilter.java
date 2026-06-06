package br.com.fiap.javaadv.VeloSpace.infrastructure.security;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import br.com.fiap.javaadv.VeloSpace.infrastructure.mongo.repository.OperatorRefRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class SecurityFilter extends OncePerRequestFilter {

    private final JwtHelper jwtHelper;

    private final OperatorRefRepository operatorRefRepository;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws IOException, ServletException {

        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = authHeader.substring("Bearer ".length());
        Optional<JwtUserData> user = jwtHelper.validateToken(token);

        if (user.isPresent()) {
            JwtUserData userData = user.get();

            if ("OPERATOR".equals(userData.role())) {
                String path = request.getRequestURI();

                boolean allowedForUnapprovedOperator = path.equals("/api/v1/operators/me") ||
                        path.matches("/api/v1/operators/\\d+/reapply");

                boolean approved = operatorRefRepository
                        .findByUserAccountId(userData.userId())
                        .map(operator -> operator.getOperatorStatusCode().equals("APPROVED"))
                        .orElse(false);

                if (!approved && !allowedForUnapprovedOperator) {
                    response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                    response.setCharacterEncoding("UTF-8");
                    response.setContentType("application/json");
                    response.getWriter().write("""
                            {"message":"Operador não aprovado"}
                            """);
                    return;
                }
            }

            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    userData,
                    null,
                    List.of(new SimpleGrantedAuthority("ROLE_" + userData.role())));

            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(request, response);
    }

}
