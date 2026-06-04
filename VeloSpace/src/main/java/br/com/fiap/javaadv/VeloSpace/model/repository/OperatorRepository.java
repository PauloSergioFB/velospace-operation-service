package br.com.fiap.javaadv.VeloSpace.model.repository;

import br.com.fiap.javaadv.VeloSpace.model.Operator;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OperatorRepository extends JpaRepository<Operator, Long> {

    Optional<Operator> findByUserAccount_UserAccountId(Long userAccountId);

    Optional<Operator> findByCpf(String cpf);

    List<Operator> findByLaunchProvider_LaunchProviderId(Long launchProviderId);

    Page<Operator> findByLaunchProvider_LaunchProviderId(Long launchProviderId, Pageable pageable);

}
