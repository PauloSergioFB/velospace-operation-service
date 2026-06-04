package br.com.fiap.javaadv.VeloSpace.service.SatelliteStatus;

public interface SatelliteStatusService<T, ID> {

    T findByCode(String code);

    T getRequiredByCode(String code);

}
