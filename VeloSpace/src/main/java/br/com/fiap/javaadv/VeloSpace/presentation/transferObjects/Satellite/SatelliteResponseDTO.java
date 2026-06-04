package br.com.fiap.javaadv.VeloSpace.presentation.transferObjects.Satellite;

import br.com.fiap.javaadv.VeloSpace.model.Satellite;
import br.com.fiap.javaadv.VeloSpace.presentation.transferObjects.SatellitePriority.SatellitePriorityResponseDTO;
import br.com.fiap.javaadv.VeloSpace.presentation.transferObjects.SatelliteStatus.SatelliteStatusResponseDTO;
import lombok.Builder;

@Builder
public record SatelliteResponseDTO(
        Long satelliteId,
        Long shipperId,
        Long launchProviderId,
        String name,
        int height,
        int width,
        int length,
        int weight,
        String launchJustification,
        SatelliteStatusResponseDTO status,
        SatellitePriorityResponseDTO priority,
        String trackingCode) {

    public static SatelliteResponseDTO from(Satellite satellite) {
        if (satellite == null) {
            return null;
        }
        return SatelliteResponseDTO.builder()
                .satelliteId(satellite.getSatelliteId())
                .shipperId(satellite.getShipper().getShipperId())
                .launchProviderId(satellite.getLaunchProvider().getLaunchProviderId())
                .name(satellite.getName())
                .height(satellite.getHeight())
                .width(satellite.getWidth())
                .length(satellite.getLength())
                .weight(satellite.getWeight())
                .launchJustification(satellite.getLaunchJustification())
                .status(SatelliteStatusResponseDTO.from(satellite.getSatelliteStatus()))
                .priority(SatellitePriorityResponseDTO.from(satellite.getSatellitePriority()))
                .trackingCode(satellite.getTrackingCode())
                .build();
    }

}
