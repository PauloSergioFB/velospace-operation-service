package br.com.fiap.javaadv.VeloSpace.infrastructure.message.consumer;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import br.com.fiap.javaadv.VeloSpace.infrastructure.message.event.LaunchProviderCreatedEvent;
import br.com.fiap.javaadv.VeloSpace.infrastructure.message.event.LaunchProviderDeletedEvent;
import br.com.fiap.javaadv.VeloSpace.infrastructure.mongo.document.LaunchProviderRef;
import br.com.fiap.javaadv.VeloSpace.infrastructure.mongo.service.LaunchProviderRefService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class LaunchProviderEventConsumer {

    private final LaunchProviderRefService launchProviderRefService;

    @JmsListener(destination = "launch_provider.created")
    public void consumeCreated(LaunchProviderCreatedEvent event) {
        log.info("Evento recebido: {}", event);

        LaunchProviderRef document = launchProviderRefService.saveCreated(event);

        log.info("LaunchProviderRef salvo no MongoDB: {}", document.getId());
    }

    @JmsListener(destination = "launch_provider.deleted")
    public void consumeCreated(LaunchProviderDeletedEvent event) {
        log.info("Evento recebido: {}", event);

        launchProviderRefService.removeDeleted(event.launchProviderId());

        log.info("LaunchProviderRef {} excluído do MongoDB", event.launchProviderId());
    }

}
