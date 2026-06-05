package br.com.fiap.javaadv.VeloSpace.infrastructure.message.event;

import lombok.Builder;

@Builder
public record ShipperDeletedEvent(Long shipperId) {
}
