package br.com.fiap.javaadv.VeloSpace.service.UserRole;

import org.springframework.stereotype.Service;

import br.com.fiap.javaadv.VeloSpace.infrastructure.enums.Role;
import br.com.fiap.javaadv.VeloSpace.infrastructure.exceptions.NotFoundException;
import br.com.fiap.javaadv.VeloSpace.model.UserRole;
import br.com.fiap.javaadv.VeloSpace.model.repository.UserRoleRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserRoleServiceImpl implements UserRoleService<UserRole, Long> {

    private final UserRoleRepository userRoleRepository;

    public UserRole findByCode(Role code) {
        return userRoleRepository.findByCode(code)
                .orElseThrow(() -> new NotFoundException(
                        "Cargo do usuário não encontrado."));
    }

    @Override
    public UserRole getRequiredByCode(Role code) {
        return userRoleRepository.findByCode(code)
                .orElseThrow(() -> new IllegalStateException(
                        "Cargo de usuário obrigatório não encontrado na tabela de suporte. Código: " + code + "."));
    }

}
