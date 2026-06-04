package br.com.fiap.javaadv.VeloSpace.presentation.transferObjects.Operator;

import br.com.fiap.javaadv.VeloSpace.model.Operator;
import br.com.fiap.javaadv.VeloSpace.presentation.transferObjects.OperatorStatus.OperatorStatusResponseDTO;
import br.com.fiap.javaadv.VeloSpace.presentation.transferObjects.UserAccount.UserAccountResponseDTO;
import lombok.Builder;

@Builder
public record OperatorResponseDTO(
        Long operatorId,
        Long launchProviderId,
        String cpf,
        String name,
        UserAccountResponseDTO userAccount,
        OperatorStatusResponseDTO status) {

    public static OperatorResponseDTO from(
            Operator operator) {

        if (operator == null) {
            return null;
        }

        return OperatorResponseDTO.builder()
                .operatorId(operator.getOperatorId())
                .launchProviderId(operator.getLaunchProvider().getLaunchProviderId())
                .cpf(operator.getCpf())
                .name(operator.getName())
                .userAccount(UserAccountResponseDTO.from(operator.getUserAccount()))
                .status(OperatorStatusResponseDTO.from(operator.getOperatorStatus()))
                .build();
    }

}
