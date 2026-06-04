package br.com.fiap.javaadv.VeloSpace.model.repository;

import br.com.fiap.javaadv.VeloSpace.model.Satellite;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SatelliteRepository extends JpaRepository<Satellite, Long> {

    List<Satellite> findByShipper_ShipperId(Long shipperId);

    List<Satellite> findByLaunchProvider_LaunchProviderId(Long launchProviderId);

    Page<Satellite> findByLaunchProvider_LaunchProviderId(Long launchProviderId, Pageable pageable);

    Page<Satellite> findByShipper_ShipperId(Long shipperId, Pageable pageable);

}
