package br.com.fiap.javaadv.VeloSpace.infrastructure.security;

import lombok.Builder;

@Builder
public record JwtUserData(
        Long userId,
        String email,
        String role) {

}
