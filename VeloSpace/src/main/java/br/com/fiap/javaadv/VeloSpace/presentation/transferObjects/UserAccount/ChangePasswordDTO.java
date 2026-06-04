package br.com.fiap.javaadv.VeloSpace.presentation.transferObjects.UserAccount;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class ChangePasswordDTO {

    @NotBlank(message = "A senha atual não pode estar em branco")
    private String currentPassword;

    @NotBlank(message = "A nova senha não pode estar em branco")
    @Size(min = 6, max = 255, message = "A senha deve ter entre 6 e 255 caracteres")
    private String newPassword;

}
