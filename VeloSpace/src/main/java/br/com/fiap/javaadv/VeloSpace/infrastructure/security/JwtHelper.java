package br.com.fiap.javaadv.VeloSpace.infrastructure.security;

import java.util.Optional;

import org.springframework.stereotype.Component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;

import br.com.fiap.javaadv.VeloSpace.infrastructure.enums.Role;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtHelper {

    private final JwtProperties jwtProperties;

    public Optional<JwtUserData> validateToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(jwtProperties.getSecret());

            DecodedJWT decode = JWT.require(algorithm).build().verify(token);
            return Optional.of(JwtUserData.builder()
                    .userId(decode.getClaim("userId").asLong())
                    .email(decode.getSubject())
                    .role(Role.valueOf(
                            decode.getClaim("role").asString()))
                    .build());
        } catch (JWTVerificationException ex) {
            return Optional.empty();
        }
    }

}
