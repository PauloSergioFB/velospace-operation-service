package br.com.fiap.javaadv.VeloSpace.model.repository;

import br.com.fiap.javaadv.VeloSpace.model.Satellite;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SatelliteRepository extends JpaRepository<Satellite, Long> {

    List<Satellite> findByShipperId(Long shipperId);

    Page<Satellite> findByShipperId(Long shipperId, Pageable pageable);

    List<Satellite> findByLaunchProviderId(Long launchProviderId);

    Page<Satellite> findByLaunchProviderId(Long launchProviderId, Pageable pageable);

}
