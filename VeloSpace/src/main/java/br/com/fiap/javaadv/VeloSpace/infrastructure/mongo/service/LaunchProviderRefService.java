package br.com.fiap.javaadv.VeloSpace.infrastructure.mongo.service;

import org.springframework.stereotype.Service;

import br.com.fiap.javaadv.VeloSpace.infrastructure.exceptions.NotFoundException;
import br.com.fiap.javaadv.VeloSpace.infrastructure.message.event.LaunchProviderCreatedEvent;
import br.com.fiap.javaadv.VeloSpace.infrastructure.mongo.document.LaunchProviderRef;
import br.com.fiap.javaadv.VeloSpace.infrastructure.mongo.repository.LaunchProviderRefRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LaunchProviderRefService {

    private final LaunchProviderRefRepository launchProviderRefRepository;

    public LaunchProviderRef findByIdOrThrow(Long id) {
        return launchProviderRefRepository.findByLaunchProviderId(id)
                .orElseThrow(() -> new NotFoundException(
                        "Provedora de lançamento não encontrada."));
    }

    public LaunchProviderRef saveCreated(LaunchProviderCreatedEvent event) {
        LaunchProviderRef document = LaunchProviderRef.builder()
                .id("launch_provider:" + event.launchProviderId())
                .launchProviderId(event.launchProviderId())
                .userAccountId(event.userAccountId())
                .build();

        return launchProviderRefRepository.save(document);
    }

    public void removeDeleted(Long launchProviderId) {
        launchProviderRefRepository.deleteByLaunchProviderId(launchProviderId);
    }

}
