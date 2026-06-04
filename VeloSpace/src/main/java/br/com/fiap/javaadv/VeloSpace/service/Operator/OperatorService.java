package br.com.fiap.javaadv.VeloSpace.service.Operator;

import org.springframework.data.domain.Page;

import br.com.fiap.javaadv.VeloSpace.infrastructure.enums.OperatorSortField;
import br.com.fiap.javaadv.VeloSpace.infrastructure.security.JwtUserData;

public interface OperatorService<T, ID> {

    T findByIdOrThrow(ID id);

    T findById(ID id, JwtUserData authUser);

    Page<T> findAllByLaunchProviderId(
            ID id, int page, int items, OperatorSortField sortBy, String direction, JwtUserData authUser);

    T create(T o);

    T updateById(ID id, T o, JwtUserData authUser);

    void updatePasswordById(ID id, String currentPassword, String newPassword, JwtUserData authUser);

    void deleteById(ID id, JwtUserData authUser);

    void approval(ID id, boolean approval, JwtUserData authUser);

    void reapply(ID id, JwtUserData authUser);

}
