package br.com.fiap.javaadv.VeloSpace.presentation.transferObjects.Auth;

import jakarta.validation.constraints.NotEmpty;

public record AuthRequestDTO(
        @NotEmpty(message = "Email é obrigatório") String email,
        @NotEmpty(message = "Senha é obrigatória") String password) {

}
