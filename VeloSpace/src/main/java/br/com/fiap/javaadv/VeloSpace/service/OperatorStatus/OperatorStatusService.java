package br.com.fiap.javaadv.VeloSpace.service.OperatorStatus;

public interface OperatorStatusService<T, ID> {

    T findByCode(String code);

    T getRequiredByCode(String code);

}
