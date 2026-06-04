package br.com.fiap.javaadv.VeloSpace.presentation.transferObjects.LaunchProvider;

import br.com.fiap.javaadv.VeloSpace.model.LaunchProvider;
import br.com.fiap.javaadv.VeloSpace.model.UserAccount;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.br.CNPJ;

@Setter
@Getter
@Builder
public class CreateLaunchProviderDTO {

    @NotNull(message = "O CNPJ não pode ser nulo")
    @Digits(integer = 14, fraction = 0, message = "O CNPJ deve conter no máximo 14 dígitos")
    @CNPJ
    private String cnpj;

    @NotBlank(message = "A razão social não pode estar em branco")
    @Size(min = 2, max = 255, message = "A razão social deve ter entre 2 e 255 caracteres")
    private String corporateName;

    @NotBlank(message = "O e-mail não pode estar em branco")
    @Email(message = "O e-mail deve ser válido")
    @Size(max = 255, message = "O e-mail deve ter no máximo 255 caracteres")
    private String email;

    @Pattern(regexp = "^\\d{0,15}$", message = "O telefone deve conter apenas números e no máximo 15 dígitos")
    private String phone;

    @NotBlank(message = "A senha não pode estar em branco")
    @Size(min = 6, max = 255, message = "A senha deve ter entre 6 e 255 caracteres")
    private String password;

    public static LaunchProvider toEntity(CreateLaunchProviderDTO dto) {
        if (dto == null) {
            return null;
        }

        return LaunchProvider.builder()
                .corporateName(dto.getCorporateName())
                .cnpj(dto.getCnpj())
                .userAccount(
                        UserAccount.builder()
                                .email(dto.getEmail())
                                .hashedPassword(dto.getPassword())
                                .phone(dto.getPhone())
                                .build())
                .build();
    }

}
