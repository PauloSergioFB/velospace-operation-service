package br.com.fiap.javaadv.VeloSpace.infrastructure.mongo.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import br.com.fiap.javaadv.VeloSpace.infrastructure.mongo.document.ShipperRef;

public interface ShipperRefRepository extends MongoRepository<ShipperRef, String> {

    Optional<ShipperRef> findByShipperId(Long shipperId);

    Optional<ShipperRef> findByUserAccountId(Long shipperId);

    Optional<ShipperRef> deleteByShipperId(Long shipperId);

}
