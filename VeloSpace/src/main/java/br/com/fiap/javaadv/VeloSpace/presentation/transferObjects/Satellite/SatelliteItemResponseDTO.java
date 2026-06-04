package br.com.fiap.javaadv.VeloSpace.presentation.transferObjects.Satellite;

import br.com.fiap.javaadv.VeloSpace.model.Satellite;
import br.com.fiap.javaadv.VeloSpace.presentation.transferObjects.SatelliteStatus.SatelliteStatusResponseDTO;
import lombok.Builder;

@Builder
public record SatelliteItemResponseDTO(
        Long satelliteId,
        Long launchProviderId,
        String name,
        SatelliteStatusResponseDTO status) {

    public static SatelliteItemResponseDTO from(Satellite satellite) {
        if (satellite == null) {
            return null;
        }
        return SatelliteItemResponseDTO.builder()
                .satelliteId(satellite.getSatelliteId())
                .launchProviderId(satellite.getLaunchProvider().getLaunchProviderId())
                .name(satellite.getName())
                .status(SatelliteStatusResponseDTO.from(satellite.getSatelliteStatus()))
                .build();
    }

}
