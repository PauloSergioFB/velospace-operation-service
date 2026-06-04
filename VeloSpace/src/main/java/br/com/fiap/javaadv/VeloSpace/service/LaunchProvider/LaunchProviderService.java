package br.com.fiap.javaadv.VeloSpace.service.LaunchProvider;

import org.springframework.data.domain.Page;

import br.com.fiap.javaadv.VeloSpace.infrastructure.enums.LaunchProviderSortField;
import br.com.fiap.javaadv.VeloSpace.infrastructure.security.JwtUserData;

public interface LaunchProviderService<T, ID> {

    T findByIdOrThrow(ID id);

    Page<T> findAll(int page, int items, LaunchProviderSortField sortBy, String direction);

    T findById(ID id, JwtUserData authUser);

    T create(T o);

    T updateById(ID id, T o, JwtUserData authUser);

    void updatePasswordById(ID id, String currentPassword, String newPassword, JwtUserData authUser);

    void deleteById(ID id, JwtUserData authUser);

}
