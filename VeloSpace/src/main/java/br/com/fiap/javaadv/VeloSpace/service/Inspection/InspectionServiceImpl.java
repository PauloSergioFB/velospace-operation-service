package br.com.fiap.javaadv.VeloSpace.service.Inspection;

import java.time.LocalDateTime;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import br.com.fiap.javaadv.VeloSpace.infrastructure.enums.InspectionResult;
import br.com.fiap.javaadv.VeloSpace.infrastructure.exceptions.BusinessRuleException;
import br.com.fiap.javaadv.VeloSpace.infrastructure.exceptions.ForbiddenException;
import br.com.fiap.javaadv.VeloSpace.infrastructure.exceptions.NotFoundException;
import br.com.fiap.javaadv.VeloSpace.infrastructure.mongo.document.OperatorRef;
import br.com.fiap.javaadv.VeloSpace.infrastructure.mongo.service.OperatorRefService;
import br.com.fiap.javaadv.VeloSpace.infrastructure.security.JwtUserData;
import br.com.fiap.javaadv.VeloSpace.model.Inspection;
import br.com.fiap.javaadv.VeloSpace.model.Satellite;
import br.com.fiap.javaadv.VeloSpace.model.SatelliteStatus;
import br.com.fiap.javaadv.VeloSpace.model.repository.InspectionRepository;
import br.com.fiap.javaadv.VeloSpace.model.repository.SatelliteRepository;
import br.com.fiap.javaadv.VeloSpace.service.Satellite.SatelliteService;
import br.com.fiap.javaadv.VeloSpace.service.SatelliteStatus.SatelliteStatusService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InspectionServiceImpl implements InspectionService<Inspection, Long> {

    private final InspectionRepository inspectionRepository;

    private final SatelliteRepository satelliteRepository;

    private final OperatorRefService operatorRefService;

    private final SatelliteService<Satellite, Long> satelliteService;

    private final SatelliteStatusService<SatelliteStatus, Long> satelliteStatusService;

    private void validateOperatorRelated(JwtUserData authUser, Satellite satellite) {
        OperatorRef operatorRef = operatorRefService.findByUserAccountIdOrThrow(authUser.userId());

        if (operatorRef.getLaunchProviderId().equals(satellite.getLaunchProviderId())) {
            throw new ForbiddenException(
                    "Você não possui permissão para acessar esta inspeção.");
        }
    }

    private void validateCurrentSatelliteStatus(
            Satellite satellite,
            String expectedStatusCode,
            String errorMessage) {

        String currentStatusCode = satellite.getSatelliteStatus().getCode();

        if (!currentStatusCode.equals(expectedStatusCode)) {
            throw new BusinessRuleException(errorMessage);
        }
    }

    private boolean exceedsTolerance(Integer declaredValue, Integer measuredValue) {
        double maximumAllowedDifference = declaredValue * 0.05;
        return Math.abs(measuredValue - declaredValue) > maximumAllowedDifference;
    }

    private boolean shouldRejectSatellite(Inspection inspection, Satellite satellite) {
        return exceedsTolerance(satellite.getHeight(), inspection.getMeasuredHeight())
                || exceedsTolerance(satellite.getWidth(), inspection.getMeasuredWidth())
                || exceedsTolerance(satellite.getLength(), inspection.getMeasuredLength())
                || exceedsTolerance(satellite.getWeight(), inspection.getMeasuredWeight());
    }

    private void updateSatelliteStatusAfterInspection(Satellite satellite, String nextStatusCode) {
        SatelliteStatus nextStatus = satelliteStatusService.getRequiredByCode(nextStatusCode);

        satellite.setSatelliteStatus(nextStatus);
        satelliteRepository.save(satellite);
    }

    @Override
    @Cacheable(value = "inspections", key = "#id")
    public Inspection findByIdOrThrow(Long id) {
        return inspectionRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(
                        "Inspeção não encontrada."));
    }

    @Override
    public Inspection findById(Long id, JwtUserData authUser) {
        Inspection inspection = findByIdOrThrow(id);
        Satellite satellite = inspection.getSatellite();

        validateOperatorRelated(authUser, satellite);

        return inspection;
    }

    @Override
    @CacheEvict(value = { "inspections", "satellites" }, allEntries = true)
    public Inspection create(Inspection inspection, JwtUserData authUser) {
        Satellite satellite = satelliteService.findByIdOrThrow(
                inspection.getSatellite().getSatelliteId());

        validateOperatorRelated(authUser, satellite);

        validateCurrentSatelliteStatus(
                satellite,
                "PENDING_INSPECTION",
                "Só é possível realizar a inspeção de um satélite que está aguardando inspeção.");

        inspection.setSatellite(satellite);
        inspection.setInspectionDate(LocalDateTime.now());

        boolean inspectionResult = shouldRejectSatellite(inspection, satellite);

        inspection.setResult(inspectionResult ? InspectionResult.A : InspectionResult.R);

        Inspection savedInspection = inspectionRepository.save(inspection);

        updateSatelliteStatusAfterInspection(satellite, inspectionResult
                ? "INSPECTION_REJECTED"
                : "READY_FOR_LAUNCH");

        return savedInspection;
    }

}
