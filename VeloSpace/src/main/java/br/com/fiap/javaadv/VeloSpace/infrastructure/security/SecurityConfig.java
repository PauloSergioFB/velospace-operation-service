package br.com.fiap.javaadv.VeloSpace.infrastructure.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import jakarta.servlet.DispatcherType;
import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final SecurityFilter securityFilter;

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.configure(http))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorize -> authorize
                        .dispatcherTypeMatchers(DispatcherType.ERROR).permitAll()
                        .requestMatchers(
                                "/",
                                "/v3/api-docs/**",
                                "/swagger-ui.html",
                                "/swagger-ui/**")
                        .permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/v1/auth").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/v1/auth/refresh").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/v1/shippers").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/v1/launch-providers").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/v1/launch-providers").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/v1/operators").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/v1/satellite-priorities").permitAll()

                        .requestMatchers(HttpMethod.GET, "/api/v1/shippers/me")
                        .hasRole("SHIPPER")
                        .requestMatchers(HttpMethod.GET, "/api/v1/shippers/{id}")
                        .hasRole("SHIPPER")
                        .requestMatchers(HttpMethod.GET, "/api/v1/shippers/{id}/hateoas")
                        .hasRole("SHIPPER")
                        .requestMatchers(HttpMethod.GET, "/api/v1/shippers/{id}/satellites")
                        .hasRole("SHIPPER")
                        .requestMatchers(HttpMethod.PUT, "/api/v1/shippers/{id}")
                        .hasRole("SHIPPER")
                        .requestMatchers(HttpMethod.PATCH, "/api/v1/shippers/{id}/password")
                        .hasRole("SHIPPER")
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/shippers/{id}")
                        .hasRole("SHIPPER")

                        .requestMatchers(HttpMethod.GET, "/api/v1/launch-providers/me")
                        .hasRole("LAUNCH_PROVIDER")
                        .requestMatchers(HttpMethod.GET, "/api/v1/launch-providers/{id}")
                        .hasRole("LAUNCH_PROVIDER")
                        .requestMatchers(HttpMethod.GET, "/api/v1/launch-providers/{id}/operators")
                        .hasRole("LAUNCH_PROVIDER")
                        .requestMatchers(HttpMethod.GET, "/api/v1/launch-providers/{id}/satellites")
                        .hasRole("LAUNCH_PROVIDER")
                        .requestMatchers(HttpMethod.PUT, "/api/v1/launch-providers/{id}")
                        .hasRole("LAUNCH_PROVIDER")
                        .requestMatchers(HttpMethod.PATCH, "/api/v1/launch-providers/{id}/password")
                        .hasRole("LAUNCH_PROVIDER")
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/launch-providers/{id}")
                        .hasRole("LAUNCH_PROVIDER")

                        .requestMatchers(HttpMethod.GET, "/api/v1/launch-providers/me")
                        .hasRole("OPERATOR")
                        .requestMatchers(HttpMethod.GET, "/api/v1/operators/{id}")
                        .hasAnyRole("OPERATOR", "LAUNCH_PROVIDER")
                        .requestMatchers(HttpMethod.PUT, "/api/v1/operators/{id}")
                        .hasRole("OPERATOR")
                        .requestMatchers(HttpMethod.PATCH, "/api/v1/operators/{id}/password")
                        .hasRole("OPERATOR")
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/operators/{id}")
                        .hasRole("OPERATOR")
                        .requestMatchers(HttpMethod.POST, "/api/v1/operators/{id}/approval")
                        .hasRole("LAUNCH_PROVIDER")
                        .requestMatchers(HttpMethod.POST, "/api/v1/operators/{id}/reapply")
                        .hasRole("OPERATOR")

                        .requestMatchers(HttpMethod.POST, "/api/v1/satellites")
                        .hasRole("SHIPPER")
                        .requestMatchers(HttpMethod.POST, "/api/v1/satellites/{id}/approval")
                        .hasRole("OPERATOR")
                        .requestMatchers(HttpMethod.POST, "/api/v1/satellites/{id}/track")
                        .hasRole("SHIPPER")
                        .requestMatchers(HttpMethod.PUT, "/api/v1/satellites/{id}")
                        .hasRole("SHIPPER")
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/satellites/{id}")
                        .hasRole("SHIPPER")

                        .requestMatchers(HttpMethod.GET, "/api/v1/inspections/{id}")
                        .hasAnyRole("SHIPPER", "OPERATOR")
                        .requestMatchers(HttpMethod.POST, "/api/v1/inspections")
                        .hasRole("OPERATOR")

                        .anyRequest().authenticated())
                .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
            throws Exception {

        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
