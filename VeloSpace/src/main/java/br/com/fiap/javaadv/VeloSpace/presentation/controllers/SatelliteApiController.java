package br.com.fiap.javaadv.VeloSpace.presentation.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.fiap.javaadv.VeloSpace.infrastructure.security.JwtUserData;
import br.com.fiap.javaadv.VeloSpace.model.Satellite;
import br.com.fiap.javaadv.VeloSpace.presentation.transferObjects.Satellite.ApprovalSatelliteDTO;
import br.com.fiap.javaadv.VeloSpace.presentation.transferObjects.Satellite.CreateSatelliteDTO;
import br.com.fiap.javaadv.VeloSpace.presentation.transferObjects.Satellite.SatelliteResponseDTO;
import br.com.fiap.javaadv.VeloSpace.presentation.transferObjects.Satellite.TrackSatelliteDTO;
import br.com.fiap.javaadv.VeloSpace.service.Satellite.SatelliteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/satellites")
@RequiredArgsConstructor
@Tag(name = "Satellite API", description = "Endpoints para gerenciamento de Satellites")
public class SatelliteApiController {

    private final SatelliteService<Satellite, Long> satelliteService;

    @GetMapping("/{id}")
    @Operation(summary = "Buscar Satellite por ID", description = "Retorna os dados de um Satellite específico, identificado pelo seu ID.")
    public ResponseEntity<SatelliteResponseDTO> findById(
            @PathVariable Long id,
            @AuthenticationPrincipal JwtUserData authUser) {

        Satellite satellite = satelliteService.findById(id, authUser);
        satelliteService.updateTracking(satellite);
        return ResponseEntity.ok(SatelliteResponseDTO.from(satellite));
    }

    @PostMapping
    @Operation(summary = "Criar um novo Satellite", description = "Recebe os dados de um Satellite e o cria no sistema.")
    public ResponseEntity<SatelliteResponseDTO> save(
            @Valid @RequestBody CreateSatelliteDTO dto,
            @AuthenticationPrincipal JwtUserData authUser) {

        Satellite newSatellite = satelliteService.create(CreateSatelliteDTO.toEntity(dto), authUser);
        return new ResponseEntity<>(SatelliteResponseDTO.from(newSatellite),
                HttpStatus.CREATED);
    }

    @PostMapping("/{id}/approval")
    @Operation(summary = "Aprovar Satellite por ID", description = "Altera o status de aprovação do Satellite identificado pelo ID informado. Recebe flag de aprovação e o ID de prioridade; requer autenticação e validação de permissões.")
    public ResponseEntity<Void> approval(
            @PathVariable Long id,
            @Valid @RequestBody ApprovalSatelliteDTO dto,
            @AuthenticationPrincipal JwtUserData authUser) {

        satelliteService.approval(id, dto.approval(), dto.satellitePriorityId(), authUser);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/track")
    @Operation(summary = "Adicionar código de rastreamento", description = "Adiciona um código de rastreamento ao Satellite identificado pelo ID.")
    public ResponseEntity<Void> tracking(
            @PathVariable Long id,
            @Valid @RequestBody TrackSatelliteDTO dto,
            @AuthenticationPrincipal JwtUserData authUser) {

        satelliteService.addTrackingCode(id, dto.trackingCode(), authUser);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar um Satellite por ID", description = "Recebe os dados atualizados de um Satellite e o ID do Satellite a ser atualizado, e realiza a atualização no sistema.")

    public ResponseEntity<SatelliteResponseDTO> updateById(
            @PathVariable Long id,
            @Valid @RequestBody CreateSatelliteDTO dto,
            @AuthenticationPrincipal JwtUserData authUser) {

        Satellite updated = satelliteService.updateById(id,
                CreateSatelliteDTO.toEntity(dto), authUser);
        return ResponseEntity.ok(SatelliteResponseDTO.from(updated));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar um Satellite por ID", description = "Recebe o ID de um Satellite e o deleta do sistema.")
    public ResponseEntity<Void> deleteById(
            @PathVariable Long id,
            @AuthenticationPrincipal JwtUserData authUser) {

        satelliteService.deleteById(id, authUser);
        return ResponseEntity.noContent().build();
    }

}
