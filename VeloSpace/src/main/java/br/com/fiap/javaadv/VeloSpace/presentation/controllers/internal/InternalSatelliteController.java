package br.com.fiap.javaadv.VeloSpace.presentation.controllers.internal;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.fiap.javaadv.VeloSpace.infrastructure.enums.SatelliteSortField;
import br.com.fiap.javaadv.VeloSpace.model.Satellite;
import br.com.fiap.javaadv.VeloSpace.presentation.transferObjects.PageResponseDTO;
import br.com.fiap.javaadv.VeloSpace.presentation.transferObjects.Satellite.SatelliteItemResponseDTO;
import br.com.fiap.javaadv.VeloSpace.service.Satellite.SatelliteService;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.RequiredArgsConstructor;

@Hidden
@RestController
@RequestMapping("/internal/v1/satellites")
@RequiredArgsConstructor
public class InternalSatelliteController {

    private final SatelliteService<Satellite, Long> satelliteService;

    @GetMapping("/by-shipper/{shipperId}")
    public ResponseEntity<PageResponseDTO<SatelliteItemResponseDTO>> findAllByShipperId(
            @PathVariable Long shipperId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int items,
            @RequestParam(defaultValue = "operatorId") SatelliteSortField sortBy,
            @RequestParam(defaultValue = "asc") String direction) {

        Page<Satellite> satellites = satelliteService.findAllByShipperId(
                shipperId, page, items, sortBy, direction);
        return ResponseEntity.ok(PageResponseDTO.from(
                satellites.map(SatelliteItemResponseDTO::from)));
    }

    @GetMapping("/by-launch-provider/{launchProviderId}")
    public ResponseEntity<PageResponseDTO<SatelliteItemResponseDTO>> findByAllLaunchProviderId(
            @PathVariable Long launchProviderId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int items,
            @RequestParam(defaultValue = "operatorId") SatelliteSortField sortBy,
            @RequestParam(defaultValue = "asc") String direction) {

        Page<Satellite> satellites = satelliteService.findAllByLaunchProviderId(
                launchProviderId, page, items, sortBy, direction);
        return ResponseEntity.ok(PageResponseDTO.from(
                satellites.map(SatelliteItemResponseDTO::from)));

    }

}
