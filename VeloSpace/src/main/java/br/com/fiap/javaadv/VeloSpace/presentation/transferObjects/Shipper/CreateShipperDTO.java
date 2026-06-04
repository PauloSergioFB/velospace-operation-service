package br.com.fiap.javaadv.VeloSpace.presentation.transferObjects.Shipper;

import br.com.fiap.javaadv.VeloSpace.infrastructure.enums.ShipperType;
import br.com.fiap.javaadv.VeloSpace.model.Shipper;
import br.com.fiap.javaadv.VeloSpace.model.UserAccount;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class CreateShipperDTO {

    @NotBlank(message = "O tipo não pode estar em branco")
    @Pattern(regexp = "^(PF|PJ)$", message = "O tipo deve ser PF ou PJ")
    private String type;

    @NotBlank(message = "O documento não pode estar em branco")
    @Digits(integer = 14, fraction = 0, message = "O documento deve conter no máximo 14 dígitos")
    private String shipperDocument;

    @NotBlank(message = "O nome não pode estar em branco")
    @Size(min = 2, max = 255, message = "O nome deve ter entre 2 e 255 caracteres")
    private String name;

    @NotBlank(message = "O e-mail não pode estar em branco")
    @Email(message = "O e-mail deve ser válido")
    @Size(max = 255, message = "O e-mail deve ter no máximo 255 caracteres")
    private String email;

    @Pattern(regexp = "^\\d{0,15}$", message = "O telefone deve conter apenas números e no máximo 15 dígitos")
    private String phone;

    @NotBlank(message = "A senha não pode estar em branco")
    @Size(min = 6, max = 255, message = "A senha deve ter entre 6 e 255 caracteres")
    private String password;

    public static Shipper toEntity(CreateShipperDTO dto) {

        if (dto == null) {
            return null;
        }

        return Shipper.builder()
                .type(ShipperType.valueOf(dto.getType()))
                .shipperDocument(dto.getShipperDocument())
                .name(dto.getName())
                .userAccount(
                        UserAccount.builder()
                                .email(dto.getEmail())
                                .hashedPassword(dto.getPassword())
                                .phone(dto.getPhone())
                                .build())
                .build();
    }

}
