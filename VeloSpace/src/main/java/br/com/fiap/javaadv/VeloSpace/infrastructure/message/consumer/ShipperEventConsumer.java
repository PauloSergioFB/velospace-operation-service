package br.com.fiap.javaadv.VeloSpace.infrastructure.message.consumer;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import br.com.fiap.javaadv.VeloSpace.infrastructure.message.event.ShipperCreatedEvent;
import br.com.fiap.javaadv.VeloSpace.infrastructure.message.event.ShipperDeletedEvent;
import br.com.fiap.javaadv.VeloSpace.infrastructure.mongo.document.ShipperRef;
import br.com.fiap.javaadv.VeloSpace.infrastructure.mongo.service.ShipperRefService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class ShipperEventConsumer {

    private final ShipperRefService shipperRefService;

    @JmsListener(destination = "shipper.created")
    public void consumeCreated(ShipperCreatedEvent event) {
        log.info("Evento recebido: {}", event);

        ShipperRef document = shipperRefService.saveCreated(event);

        log.info("ShipperRef salvo no MongoDB: {}", document.getId());
    }

    @JmsListener(destination = "shipper.deleted")
    public void consumeCreated(ShipperDeletedEvent event) {
        log.info("Evento recebido: {}", event);

        shipperRefService.removeDeleted(event.shipperId());

        log.info("ShipperRef {} excluído do MongoDB", event.shipperId());
    }

}
