package br.com.fiap.javaadv.VeloSpace.presentation.controllers;

import br.com.fiap.javaadv.VeloSpace.model.SatellitePriority;
import br.com.fiap.javaadv.VeloSpace.presentation.transferObjects.SatellitePriority.SatellitePriorityResponseDTO;
import br.com.fiap.javaadv.VeloSpace.service.SatellitePriority.SatellitePriorityService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/v1/satellite-priorities")
@RequiredArgsConstructor
@Tag(name = "Satellite Priority API", description = "Endpoints para gerenciamento de Prioridades de Satellite")
public class SatellitePriorityApiController {

    private final SatellitePriorityService<SatellitePriority, Long> satellitePriorityService;

    @GetMapping
    @Operation(summary = "Listar todas as prioridades de satellite", description = "Retorna uma lista de todas as prioridades de satellite disponíveis no sistema.")
    public ResponseEntity<List<SatellitePriorityResponseDTO>> findAll() {
        List<SatellitePriorityResponseDTO> priorities = satellitePriorityService.findAll()
                .stream()
                .map(SatellitePriorityResponseDTO::from)
                .toList();
        return ResponseEntity.ok(priorities);
    }

}
