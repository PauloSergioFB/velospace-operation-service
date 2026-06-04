package br.com.fiap.javaadv.VeloSpace.presentation.transferObjects.Operator;

import br.com.fiap.javaadv.VeloSpace.model.Operator;
import br.com.fiap.javaadv.VeloSpace.presentation.transferObjects.OperatorStatus.OperatorStatusResponseDTO;
import lombok.Builder;

@Builder
public record OperatorItemResponseDTO(
        Long operatorId,
        Long launchProviderId,
        String name,
        OperatorStatusResponseDTO status) {

    public static OperatorItemResponseDTO from(
            Operator operator) {

        if (operator == null) {
            return null;
        }

        return OperatorItemResponseDTO.builder()
                .operatorId(operator.getOperatorId())
                .launchProviderId(operator.getLaunchProvider().getLaunchProviderId())
                .name(operator.getName())
                .status(OperatorStatusResponseDTO.from(operator.getOperatorStatus()))
                .build();
    }

}
