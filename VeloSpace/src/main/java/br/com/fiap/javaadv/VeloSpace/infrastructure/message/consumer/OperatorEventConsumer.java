package br.com.fiap.javaadv.VeloSpace.infrastructure.message.consumer;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import br.com.fiap.javaadv.VeloSpace.infrastructure.message.event.OperatorCreatedEvent;
import br.com.fiap.javaadv.VeloSpace.infrastructure.message.event.OperatorDeletedEvent;
import br.com.fiap.javaadv.VeloSpace.infrastructure.message.event.OperatorUpdatedEvent;
import br.com.fiap.javaadv.VeloSpace.infrastructure.mongo.document.OperatorRef;
import br.com.fiap.javaadv.VeloSpace.infrastructure.mongo.service.OperatorRefService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class OperatorEventConsumer {

    private final OperatorRefService operatorRefService;

    @JmsListener(destination = "operator.created")
    public void consumeCreated(OperatorCreatedEvent event) {
        log.info("Evento recebido: {}", event);

        OperatorRef document = operatorRefService.saveCreated(event);

        log.info("OperatorRef salvo no MongoDB: {}", document.getId());
    }

    @JmsListener(destination = "operator.updated")
    public void consumeUpdated(OperatorUpdatedEvent event) {
        log.info("Evento recebido: {}", event);

        operatorRefService.saveUpdated(event);

        log.info("OperatorRef {} excluído do MongoDB", event.operatorId());
    }

    @JmsListener(destination = "operator.deleted")
    public void consumeCreated(OperatorDeletedEvent event) {
        log.info("Evento recebido: {}", event);

        operatorRefService.removeDeleted(event.operatorId());

        log.info("OperatorRef {} excluído do MongoDB", event.operatorId());
    }

}
