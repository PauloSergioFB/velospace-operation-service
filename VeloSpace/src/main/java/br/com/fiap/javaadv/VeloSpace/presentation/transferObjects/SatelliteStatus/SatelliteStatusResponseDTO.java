package br.com.fiap.javaadv.VeloSpace.presentation.transferObjects.SatelliteStatus;

import br.com.fiap.javaadv.VeloSpace.model.SatelliteStatus;
import lombok.Builder;

@Builder
public record SatelliteStatusResponseDTO(
        Long satelliteStatusId,
        String code,
        String description) {

    public static SatelliteStatusResponseDTO from(SatelliteStatus satelliteStatus) {
        if (satelliteStatus == null) {
            return null;
        }
        return SatelliteStatusResponseDTO.builder()
                .satelliteStatusId(satelliteStatus.getSatelliteStatusId())
                .code(satelliteStatus.getCode())
                .description(satelliteStatus.getDescription())
                .build();
    }

}
