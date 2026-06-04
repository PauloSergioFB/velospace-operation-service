package br.com.fiap.javaadv.VeloSpace.model.repository;

import br.com.fiap.javaadv.VeloSpace.model.LaunchProvider;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LaunchProviderRepository extends JpaRepository<LaunchProvider, Long> {

    Page<LaunchProvider> findAll(Pageable pageable);

    Optional<LaunchProvider> findByCnpj(String cnpj);

}
