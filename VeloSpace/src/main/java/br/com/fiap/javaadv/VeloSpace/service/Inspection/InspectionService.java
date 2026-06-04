package br.com.fiap.javaadv.VeloSpace.service.Inspection;

import br.com.fiap.javaadv.VeloSpace.infrastructure.security.JwtUserData;

public interface InspectionService<T, ID> {

    T findByIdOrThrow(ID id);

    T findById(ID id, JwtUserData authUser);

    T create(T o, JwtUserData authUser);

}
