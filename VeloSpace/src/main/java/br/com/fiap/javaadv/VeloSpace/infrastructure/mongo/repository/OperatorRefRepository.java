package br.com.fiap.javaadv.VeloSpace.infrastructure.mongo.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import br.com.fiap.javaadv.VeloSpace.infrastructure.mongo.document.OperatorRef;

public interface OperatorRefRepository extends MongoRepository<OperatorRef, String> {

    Optional<OperatorRef> findByOperatorId(Long operatorId);

    Optional<OperatorRef> findByUserAccountId(Long operatorId);

    Optional<OperatorRef> deleteByOperatorId(Long operatorId);

}
