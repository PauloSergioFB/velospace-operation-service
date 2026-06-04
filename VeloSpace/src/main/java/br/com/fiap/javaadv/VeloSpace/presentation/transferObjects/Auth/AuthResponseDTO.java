package br.com.fiap.javaadv.VeloSpace.presentation.transferObjects.Auth;

public record AuthResponseDTO(
        String token,
        String refreshToken) {

}
