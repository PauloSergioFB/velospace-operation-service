package br.com.fiap.javaadv.VeloSpace.presentation.transferObjects.Inspection;

import br.com.fiap.javaadv.VeloSpace.infrastructure.enums.InspectionResult;
import br.com.fiap.javaadv.VeloSpace.model.Inspection;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record InspectionResponseDTO(
        Long inspectionId,
        Long satelliteId,
        Long operatorId,
        int measuredHeight,
        int measuredWidth,
        int measuredLength,
        int measuredWeight,
        InspectionResult result,
        LocalDateTime inspectionDate) {

    public static InspectionResponseDTO from(Inspection inspection) {
        if (inspection == null) {
            return null;
        }

        return InspectionResponseDTO.builder()
                .inspectionId(inspection.getInspectionId())
                .satelliteId(inspection.getSatellite().getSatelliteId())
                .operatorId(inspection.getOperator().getOperatorId())
                .measuredHeight(inspection.getMeasuredHeight())
                .measuredWidth(inspection.getMeasuredWidth())
                .measuredLength(inspection.getMeasuredLength())
                .measuredWeight(inspection.getMeasuredWeight())
                .result(inspection.getResult())
                .inspectionDate(inspection.getInspectionDate())
                .build();
    }

}
