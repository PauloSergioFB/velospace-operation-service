package br.com.fiap.javaadv.VeloSpace.infrastructure.message.event;

import lombok.Builder;

@Builder
public record OperatorDeletedEvent(Long operatorId) {
}
