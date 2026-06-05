package br.com.fiap.javaadv.VeloSpace.infrastructure.mongo.service;

import org.springframework.stereotype.Service;

import br.com.fiap.javaadv.VeloSpace.infrastructure.exceptions.NotFoundException;
import br.com.fiap.javaadv.VeloSpace.infrastructure.message.event.ShipperCreatedEvent;
import br.com.fiap.javaadv.VeloSpace.infrastructure.mongo.document.ShipperRef;
import br.com.fiap.javaadv.VeloSpace.infrastructure.mongo.repository.ShipperRefRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ShipperRefService {

    private final ShipperRefRepository shipperRefRepository;

    public ShipperRef findByIdOrThrow(Long id) {
        return shipperRefRepository.findByShipperId(id)
                .orElseThrow(() -> new NotFoundException(
                        "Expedidor não encontrado."));
    }

    public ShipperRef findByUserAccountIdOrThrow(Long id) {
        return shipperRefRepository.findByUserAccountId(id)
                .orElseThrow(() -> new NotFoundException(
                        "Expedidor não encontrado."));
    }

    public ShipperRef saveCreated(ShipperCreatedEvent event) {
        ShipperRef document = ShipperRef.builder()
                .id("shipper:" + event.shipperId())
                .shipperId(event.shipperId())
                .userAccountId(event.userAccountId())
                .build();

        return shipperRefRepository.save(document);
    }

    public void removeDeleted(Long shipperId) {
        shipperRefRepository.deleteByShipperId(shipperId);
    }

}
