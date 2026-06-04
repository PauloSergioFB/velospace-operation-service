package br.com.fiap.javaadv.VeloSpace.presentation.transferObjects.SatellitePriority;

import br.com.fiap.javaadv.VeloSpace.model.SatellitePriority;
import lombok.Builder;

@Builder
public record SatellitePriorityResponseDTO(
        Long satellitePriorityId,
        Long priorityLevel,
        String description) {

    public static SatellitePriorityResponseDTO from(SatellitePriority satellitePriority) {
        if (satellitePriority == null) {
            return null;
        }
        return SatellitePriorityResponseDTO.builder()
                .satellitePriorityId(satellitePriority.getSatellitePriorityId())
                .priorityLevel(satellitePriority.getPriority_level())
                .description(satellitePriority.getDescription())
                .build();
    }

}
