package br.com.fiap.javaadv.VeloSpace.model.repository;

import br.com.fiap.javaadv.VeloSpace.model.SatelliteStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SatelliteStatusRepository extends JpaRepository<SatelliteStatus, Long> {

    Optional<SatelliteStatus> findByCode(String code);

}
