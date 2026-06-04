package br.com.fiap.javaadv.VeloSpace.presentation.transferObjects.Satellite;

import jakarta.validation.constraints.NotNull;

public record TrackSatelliteDTO(
        @NotNull String trackingCode) {
}
