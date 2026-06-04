package br.com.fiap.javaadv.VeloSpace.service.SatellitePriority;

import java.util.List;

public interface SatellitePriorityService<T, ID> {

    List<T> findAll();

    T findByIdOrThrow(ID id);

}
