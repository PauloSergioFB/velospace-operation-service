package br.com.fiap.javaadv.VeloSpace.service.UserRole;

import br.com.fiap.javaadv.VeloSpace.infrastructure.enums.Role;

public interface UserRoleService<T, ID> {

    T findByCode(Role code);

    T getRequiredByCode(Role code);

}
