package br.com.fiap.javaadv.VeloSpace.service.Satellite;

import br.com.fiap.javaadv.VeloSpace.infrastructure.clients.delivery.DeliveryFeignClient;
import br.com.fiap.javaadv.VeloSpace.infrastructure.clients.delivery.transferObjects.TrackResponseDTO;
import br.com.fiap.javaadv.VeloSpace.infrastructure.enums.Role;
import br.com.fiap.javaadv.VeloSpace.infrastructure.enums.SatelliteSortField;
import br.com.fiap.javaadv.VeloSpace.infrastructure.exceptions.BusinessRuleException;
import br.com.fiap.javaadv.VeloSpace.infrastructure.exceptions.FieldValidationException;
import br.com.fiap.javaadv.VeloSpace.infrastructure.exceptions.ForbiddenException;
import br.com.fiap.javaadv.VeloSpace.infrastructure.exceptions.NotFoundException;
import br.com.fiap.javaadv.VeloSpace.infrastructure.security.JwtUserData;
import br.com.fiap.javaadv.VeloSpace.model.LaunchProvider;
import br.com.fiap.javaadv.VeloSpace.model.Satellite;
import br.com.fiap.javaadv.VeloSpace.model.SatellitePriority;
import br.com.fiap.javaadv.VeloSpace.model.Operator;
import br.com.fiap.javaadv.VeloSpace.model.SatelliteStatus;
import br.com.fiap.javaadv.VeloSpace.model.Shipper;
import br.com.fiap.javaadv.VeloSpace.model.repository.SatelliteRepository;
import br.com.fiap.javaadv.VeloSpace.service.LaunchProvider.LaunchProviderService;
import br.com.fiap.javaadv.VeloSpace.service.Operator.OperatorService;
import br.com.fiap.javaadv.VeloSpace.service.SatellitePriority.SatellitePriorityService;
import br.com.fiap.javaadv.VeloSpace.service.SatelliteStatus.SatelliteStatusService;
import br.com.fiap.javaadv.VeloSpace.service.Shipper.ShipperService;
import feign.FeignException;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class SatelliteServiceImpl implements SatelliteService<Satellite, Long> {

    private final SatelliteRepository satelliteRepository;

    private final ShipperService<Shipper, Long> shipperService;

    private final OperatorService<Operator, Long> operatorService;

    private final LaunchProviderService<LaunchProvider, Long> launchProviderService;

    private final SatelliteStatusService<SatelliteStatus, Long> satelliteStatusService;

    private final SatellitePriorityService<SatellitePriority, Long> satellitePriorityService;

    private DeliveryFeignClient deliveryClient;

    private void validateShipperOwner(JwtUserData authUser, Satellite satellite) {
        Long shipperUserAccountId = satellite.getShipper().getUserAccount().getUserAccountId();

        if (!Objects.equals(authUser.userId(), shipperUserAccountId)) {
            throw new ForbiddenException(
                    "Você não possui permissão para acessar este satélite.");
        }
    }

    private void validateOperatorRelated(JwtUserData authUser, Satellite satellite) {
        Operator operator = operatorService.findByIdOrThrow(authUser.userId());

        if (!Objects.equals(operator.getLaunchProvider(), satellite.getLaunchProvider())) {
            throw new ForbiddenException(
                    "Você não possui permissão para acessar este satélite.");
        }
    }

    private void validateAccess(JwtUserData authUser, Satellite satellite) {
        if (authUser.role().equals(Role.SHIPPER)) {
            validateShipperOwner(authUser, satellite);
            return;
        }

        if (authUser.role().equals(Role.OPERATOR)) {
            validateOperatorRelated(authUser, satellite);
            return;
        }

        throw new ForbiddenException(
                "Você não possui permissão para acessar este satélite.");
    }

    private void validateCurrentStatus(Satellite satellite, String expectedStatusCode, String errorMessage) {
        String currentStatusCode = satellite.getSatelliteStatus().getCode();

        if (!currentStatusCode.equals(expectedStatusCode)) {
            throw new BusinessRuleException(errorMessage);
        }
    }

    private void validateCurrentStatusIn(Satellite satellite, List<String> allowedStatusCodes, String errorMessage) {
        String currentStatusCode = satellite.getSatelliteStatus().getCode();

        if (!allowedStatusCodes.contains(currentStatusCode)) {
            throw new BusinessRuleException(errorMessage);
        }
    }

    private void changeStatus(Satellite satellite, String statusCode) {
        SatelliteStatus status = satelliteStatusService.getRequiredByCode(statusCode);
        satellite.setSatelliteStatus(status);
        satelliteRepository.save(satellite);
    }

    private void changeSatellitePriority(Satellite satellite, Long satellitePriorityId) {
        SatellitePriority priority = satellitePriorityService.findByIdOrThrow(satellitePriorityId);
        satellite.setSatellitePriority(priority);
        satelliteRepository.save(satellite);
    }

    private String trackSatellite(String trackingCode) {
        TrackResponseDTO trackResponse;

        try {
            trackResponse = deliveryClient.track(trackingCode);
        } catch (FeignException.NotFound exception) {
            throw new FieldValidationException(
                    "trackingCode",
                    "Código de rastreio inválido ou não encontrado.");
        }

        if (trackResponse == null || trackResponse.status() == null) {
            throw new FieldValidationException(
                    "trackingCode",
                    "Código de rastreio inválido.");
        }

        return trackResponse.status();
    }

    @Override
    public Satellite findByIdOrThrow(Long id) {
        return satelliteRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(
                        "Satélite não encontrado."));
    }

    @Override
    public Satellite findById(Long id, JwtUserData authUser) {
        Satellite satellite = findByIdOrThrow(id);
        validateAccess(authUser, satellite);

        return satellite;
    }

    @Override
    public Page<Satellite> findAllByLaunchProviderId(
            Long launchProviderId,
            int page,
            int items,
            SatelliteSortField sortBy,
            String direction,
            JwtUserData authUser) {

        launchProviderService.findById(launchProviderId, authUser);

        Sort sort = direction.equalsIgnoreCase("desc")
                ? Sort.by(sortBy.name()).descending()
                : Sort.by(sortBy.name()).ascending();

        return satelliteRepository.findByLaunchProvider_LaunchProviderId(
                launchProviderId, PageRequest.of(page, items, sort));
    }

    @Override
    public Page<Satellite> findAllByShipperId(
            Long shipperId,
            int page,
            int items,
            SatelliteSortField sortBy,
            String direction,
            JwtUserData authUser) {

        shipperService.findById(shipperId, authUser);

        Sort sort = direction.equalsIgnoreCase("desc")
                ? Sort.by(sortBy.name()).descending()
                : Sort.by(sortBy.name()).ascending();

        return satelliteRepository.findByShipper_ShipperId(
                shipperId, PageRequest.of(page, items, sort));
    }

    @Override
    public Satellite create(Satellite satellite, JwtUserData authUser) {
        Shipper shipper = shipperService.findByUserAccountIdOrThrow(authUser.userId());

        LaunchProvider launchProvider = launchProviderService.findByIdOrThrow(
                satellite.getLaunchProvider().getLaunchProviderId());

        SatelliteStatus status = satelliteStatusService.getRequiredByCode("PENDING_APPROVAL");

        satellite.setShipper(shipper);
        satellite.setLaunchProvider(launchProvider);
        satellite.setSatelliteStatus(status);

        return satelliteRepository.save(satellite);
    }

    @Override
    public void approval(Long id, boolean approved, Long satellitePriorityId, JwtUserData authUser) {
        Satellite satellite = findByIdOrThrow(id);

        validateOperatorRelated(authUser, satellite);

        validateCurrentStatus(
                satellite,
                "PENDING_APPROVAL",
                "Só é possível aprovar ou reprovar um satélite que está aguardando aprovação.");

        changeStatus(satellite, approved ? "AWAITING_SHIPMENT" : "REJECTED");

        if (approved) {
            if (satellitePriorityId == null) {
                throw new FieldValidationException(
                        "satellitePriorityId",
                        "A prioridade do satélite é obrigatória quando ele for aprovado.");
            }

            changeSatellitePriority(satellite, satellitePriorityId);
        }
    }

    @Override
    public void addTrackingCode(Long id, String trackingCode, JwtUserData authUser) {
        Satellite satellite = findByIdOrThrow(id);

        validateShipperOwner(authUser, satellite);

        validateCurrentStatus(
                satellite,
                "AWAITING_SHIPMENT",
                "Só é possível adicionar um código de rastreio a um satélite que está aguardando envio.");

        String trackStatus = trackSatellite(trackingCode);

        satellite.setTrackingCode(trackingCode);
        changeStatus(satellite, trackStatus);

        satelliteRepository.save(satellite);
    }

    @Override
    public Satellite updateById(Long id, Satellite satellite, JwtUserData authUser) {
        Satellite existing = findByIdOrThrow(id);

        validateShipperOwner(authUser, existing);

        validateCurrentStatus(
                existing,
                "PENDING_APPROVAL",
                "Só é possível alterar um satélite que está aguardando aprovação.");

        LaunchProvider launchProvider = launchProviderService.findByIdOrThrow(
                satellite.getLaunchProvider().getLaunchProviderId());

        existing.setName(satellite.getName());
        existing.setHeight(satellite.getHeight());
        existing.setWidth(satellite.getWidth());
        existing.setLength(satellite.getLength());
        existing.setWeight(satellite.getWeight());
        existing.setLaunchJustification(satellite.getLaunchJustification());
        existing.setLaunchProvider(launchProvider);

        return satelliteRepository.save(existing);
    }

    @Override
    public void deleteById(Long id, JwtUserData authUser) {
        Satellite satellite = findByIdOrThrow(id);

        validateShipperOwner(authUser, satellite);

        validateCurrentStatusIn(
                satellite,
                List.of(
                        "PENDING_APPROVAL",
                        "REJECTED",
                        "AWAITING_SHIPMENT",
                        "INSPECTION_REJECTED"),
                "Não é possível excluir um satélite neste status.");

        satelliteRepository.delete(satellite);
    }

    @Override
    public void updateTracking(Satellite satellite) {
        List<String> deliveryStatusCode = List.of(
                "AWAITING_DISPATCH", "IN_TRANSIT");

        if (deliveryStatusCode.contains(satellite.getSatelliteStatus().getCode())) {
            String trackStatus = trackSatellite(satellite.getTrackingCode());
            changeStatus(satellite, trackStatus);
            satelliteRepository.save(satellite);
        }
    }

}
