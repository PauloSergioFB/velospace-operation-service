package br.com.fiap.javaadv.VeloSpace.service.Shipper;

import br.com.fiap.javaadv.VeloSpace.infrastructure.security.JwtUserData;

public interface ShipperService<T, ID> {

    T findByIdOrThrow(ID id);

    T findByUserAccountIdOrThrow(ID id);

    T findById(ID id, JwtUserData authUser);

    T findByUserAccountId(ID id, JwtUserData authUser);

    T create(T o);

    T updateById(ID id, T o, JwtUserData authUser);

    void updatePasswordById(ID id, String currentPassword, String newPassword, JwtUserData authUser);

    void deleteById(ID id, JwtUserData authUser);

}
