package br.com.fiap.javaadv.VeloSpace.model.repository;

import br.com.fiap.javaadv.VeloSpace.model.Shipper;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ShipperRepository extends JpaRepository<Shipper, Long> {

    Optional<Shipper> findByUserAccount_UserAccountId(Long id);

    Optional<Shipper> findByShipperDocument(String shipperDocument);
}
