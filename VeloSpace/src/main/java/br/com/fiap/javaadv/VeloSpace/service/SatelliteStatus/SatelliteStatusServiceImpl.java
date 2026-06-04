package br.com.fiap.javaadv.VeloSpace.service.SatelliteStatus;

import org.springframework.stereotype.Service;

import br.com.fiap.javaadv.VeloSpace.infrastructure.exceptions.NotFoundException;
import br.com.fiap.javaadv.VeloSpace.model.SatelliteStatus;
import br.com.fiap.javaadv.VeloSpace.model.repository.SatelliteStatusRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SatelliteStatusServiceImpl implements SatelliteStatusService<SatelliteStatus, Long> {

    private final SatelliteStatusRepository satelliteRepository;

    @Override
    public SatelliteStatus findByCode(String code) {
        return satelliteRepository.findByCode(code)
                .orElseThrow(() -> new NotFoundException(
                        "Status de satélite não encontrado."));
    }

    @Override
    public SatelliteStatus getRequiredByCode(String code) {
        return satelliteRepository.findByCode(code)
                .orElseThrow(() -> new IllegalStateException(
                        "Status de satélite obrigatório não encontrado na base de dados: "
                                + code + "."));
    }

}
