package br.com.fiap.javaadv.VeloSpace.infrastructure.message.event;

import lombok.Builder;

@Builder
public record OperatorUpdatedEvent(
        Long operatorId,
        Long launchProviderId,
        Long userAccountId,
        String operatorStatusCode) {
}
