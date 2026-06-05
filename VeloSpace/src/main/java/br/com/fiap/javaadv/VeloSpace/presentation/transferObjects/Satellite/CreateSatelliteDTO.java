package br.com.fiap.javaadv.VeloSpace.presentation.transferObjects.Satellite;

import br.com.fiap.javaadv.VeloSpace.model.*;
import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class CreateSatelliteDTO {

    @NotNull(message = "O provedor de lançamento não pode ser nulo")
    private Long launchProviderId;

    @NotBlank(message = "O nome não pode estar em branco")
    @Size(min = 2, max = 55, message = "O nome deve ter entre 2 e 55 caracteres")
    private String name;

    @NotNull(message = "A altura não pode ser nula")
    @Positive(message = "A altura deve ser maior que zero")
    private Integer height;

    @NotNull(message = "A largura não pode ser nula")
    @Positive(message = "A largura deve ser maior que zero")
    private Integer width;

    @NotNull(message = "O comprimento não pode ser nulo")
    @Positive(message = "O comprimento deve ser maior que zero")
    private Integer length;

    @NotNull(message = "O peso não pode ser nulo")
    @Positive(message = "O peso deve ser maior que zero")
    private Integer weight;

    @NotBlank(message = "A justificativa de lançamento não pode estar em branco")
    @Size(max = 500, message = "A justificativa deve ter no máximo 500 caracteres")
    private String launchJustification;

    public static Satellite toEntity(CreateSatelliteDTO dto) {

        if (dto == null) {
            return null;
        }

        return Satellite.builder()
                .launchProviderId(dto.getLaunchProviderId())
                .name(dto.getName())
                .height(dto.getHeight())
                .width(dto.getWidth())
                .length(dto.getLength())
                .weight(dto.getWeight())
                .launchJustification(dto.getLaunchJustification())
                .build();
    }

}
