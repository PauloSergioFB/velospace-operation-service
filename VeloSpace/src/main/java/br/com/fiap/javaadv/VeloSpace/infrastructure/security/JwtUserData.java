package br.com.fiap.javaadv.VeloSpace.infrastructure.security;

import br.com.fiap.javaadv.VeloSpace.infrastructure.enums.Role;
import lombok.Builder;

@Builder
public record JwtUserData(
                Long userId,
                String email,
                Role role) {

}
