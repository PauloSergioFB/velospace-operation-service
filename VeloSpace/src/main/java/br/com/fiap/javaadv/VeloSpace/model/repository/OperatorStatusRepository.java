package br.com.fiap.javaadv.VeloSpace.model.repository;

import br.com.fiap.javaadv.VeloSpace.model.OperatorStatus;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface OperatorStatusRepository extends JpaRepository<OperatorStatus, Long> {

    Optional<OperatorStatus> findByCode(String code);

}
