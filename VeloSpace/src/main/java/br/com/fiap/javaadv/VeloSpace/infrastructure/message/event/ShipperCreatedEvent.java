package br.com.fiap.javaadv.VeloSpace.infrastructure.message.event;

public record ShipperCreatedEvent(
        Long shipperId,
        Long userAccountId,
        String name) {
}