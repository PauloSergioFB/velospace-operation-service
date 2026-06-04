package br.com.fiap.javaadv.VeloSpace.presentation.transferObjects.OperatorStatus;

import br.com.fiap.javaadv.VeloSpace.model.OperatorStatus;
import lombok.Builder;

@Builder
public record OperatorStatusResponseDTO(
        Long operatorStatusId,
        String code,
        String description) {

    public static OperatorStatusResponseDTO from(
            OperatorStatus operatorStatus) {

        if (operatorStatus == null) {
            return null;
        }

        return OperatorStatusResponseDTO.builder()
                .operatorStatusId(operatorStatus.getOperatorStatusId())
                .code(operatorStatus.getCode())
                .description(operatorStatus.getDescription())
                .build();
    }

}
