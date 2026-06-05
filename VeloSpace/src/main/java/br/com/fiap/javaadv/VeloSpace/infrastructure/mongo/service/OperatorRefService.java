package br.com.fiap.javaadv.VeloSpace.infrastructure.mongo.service;

import org.springframework.stereotype.Service;

import br.com.fiap.javaadv.VeloSpace.infrastructure.exceptions.NotFoundException;
import br.com.fiap.javaadv.VeloSpace.infrastructure.message.event.OperatorCreatedEvent;
import br.com.fiap.javaadv.VeloSpace.infrastructure.message.event.OperatorUpdatedEvent;
import br.com.fiap.javaadv.VeloSpace.infrastructure.mongo.document.OperatorRef;
import br.com.fiap.javaadv.VeloSpace.infrastructure.mongo.repository.OperatorRefRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OperatorRefService {

    private final OperatorRefRepository operatorRefRepository;

    public OperatorRef findByIdOrThrow(Long id) {
        return operatorRefRepository.findByOperatorId(id)
                .orElseThrow(() -> new NotFoundException(
                        "Operador não encontrado."));
    }

    public OperatorRef findByUserAccountIdOrThrow(Long id) {
        return operatorRefRepository.findByUserAccountId(id)
                .orElseThrow(() -> new NotFoundException(
                        "Operador não encontrado."));
    }

    public OperatorRef saveCreated(OperatorCreatedEvent event) {
        OperatorRef document = OperatorRef.builder()
                .id("operator:" + event.operatorId())
                .operatorId(event.operatorId())
                .userAccountId(event.userAccountId())
                .operatorStatusCode(event.operatorStatusCode())
                .build();

        return operatorRefRepository.save(document);
    }

    public OperatorRef saveUpdated(OperatorUpdatedEvent event) {
        OperatorRef operatorRef = findByIdOrThrow(event.operatorId());

        operatorRef.setLaunchProviderId(event.launchProviderId());
        operatorRef.setUserAccountId(event.userAccountId());
        operatorRef.setOperatorStatusCode(event.operatorStatusCode());

        return operatorRefRepository.save(operatorRef);
    }

    public void removeDeleted(Long operatorId) {
        operatorRefRepository.deleteByOperatorId(operatorId);
    }

}
