package br.com.fiap.javaadv.VeloSpace.infrastructure.mongo.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import br.com.fiap.javaadv.VeloSpace.infrastructure.mongo.document.LaunchProviderRef;

public interface LaunchProviderRefRepository extends MongoRepository<LaunchProviderRef, String> {

    Optional<LaunchProviderRef> deleteByLaunchProviderId(Long launchProviderId);

    Optional<LaunchProviderRef> findByLaunchProviderId(Long launchProviderId);

}
