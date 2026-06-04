package br.com.fiap.javaadv.VeloSpace.presentation.transferObjects.Auth;

import jakarta.validation.constraints.NotEmpty;

public record RefreshTokenRequestDTO(
        @NotEmpty(message = "Refresh Token é obrigatório") String refreshToken) {

}
