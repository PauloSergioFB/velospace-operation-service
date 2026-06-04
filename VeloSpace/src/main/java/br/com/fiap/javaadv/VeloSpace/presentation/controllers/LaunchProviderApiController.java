package br.com.fiap.javaadv.VeloSpace.presentation.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import br.com.fiap.javaadv.VeloSpace.infrastructure.enums.LaunchProviderSortField;
import br.com.fiap.javaadv.VeloSpace.infrastructure.enums.OperatorSortField;
import br.com.fiap.javaadv.VeloSpace.infrastructure.enums.SatelliteSortField;
import br.com.fiap.javaadv.VeloSpace.infrastructure.security.JwtUserData;
import br.com.fiap.javaadv.VeloSpace.model.LaunchProvider;
import br.com.fiap.javaadv.VeloSpace.model.Operator;
import br.com.fiap.javaadv.VeloSpace.model.Satellite;
import br.com.fiap.javaadv.VeloSpace.presentation.transferObjects.PageResponseDTO;
import br.com.fiap.javaadv.VeloSpace.presentation.transferObjects.LaunchProvider.CreateLaunchProviderDTO;
import br.com.fiap.javaadv.VeloSpace.presentation.transferObjects.LaunchProvider.LaunchProviderItemResponseDTO;
import br.com.fiap.javaadv.VeloSpace.presentation.transferObjects.LaunchProvider.LaunchProviderResponseDTO;
import br.com.fiap.javaadv.VeloSpace.presentation.transferObjects.Operator.OperatorItemResponseDTO;
import br.com.fiap.javaadv.VeloSpace.presentation.transferObjects.Satellite.SatelliteItemResponseDTO;
import br.com.fiap.javaadv.VeloSpace.presentation.transferObjects.UserAccount.ChangePasswordDTO;
import br.com.fiap.javaadv.VeloSpace.service.LaunchProvider.LaunchProviderService;
import br.com.fiap.javaadv.VeloSpace.service.Operator.OperatorService;
import br.com.fiap.javaadv.VeloSpace.service.Satellite.SatelliteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/launch-providers")
@Tag(name = "Launch Providers API", description = "Endpoints para gerenciamento de Launch Providers")
public class LaunchProviderApiController {

    private final LaunchProviderService<LaunchProvider, Long> launchProviderService;

    private final OperatorService<Operator, Long> operatorService;

    private final SatelliteService<Satellite, Long> satelliteService;

    @GetMapping("/me")
    @Operation(summary = "Buscar meu Launch Provider", description = "Retorna os dados do Launch Provider vinculado ao usuário autenticado.")
    public ResponseEntity<LaunchProviderResponseDTO> findByMe(
            @PathVariable Long id,
            @AuthenticationPrincipal JwtUserData authUser) {

        LaunchProvider launchProvider = launchProviderService.findById(authUser.userId(), authUser);
        return ResponseEntity.ok(LaunchProviderResponseDTO.from(launchProvider));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar Launch Provider por ID", description = "Retorna os dados de um Launch Provider específico, identificado pelo seu ID.")
    public ResponseEntity<LaunchProviderResponseDTO> findById(
            @PathVariable Long id,
            @AuthenticationPrincipal JwtUserData authUser) {

        LaunchProvider launchProvider = launchProviderService.findById(id, authUser);
        return ResponseEntity.ok(LaunchProviderResponseDTO.from(launchProvider));
    }

    @GetMapping
    @Operation(summary = "Listar todos os Launch Providers", description = "Retorna os dados de todos os Launch Providers cadastrados.")
    public ResponseEntity<PageResponseDTO<LaunchProviderItemResponseDTO>> findAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int items,
            @RequestParam(defaultValue = "launchProviderId") LaunchProviderSortField sortBy,
            @RequestParam(defaultValue = "asc") String direction) {

        Page<LaunchProvider> launchProviders = launchProviderService.findAll(
                page, items, sortBy, direction);
        return ResponseEntity.ok(PageResponseDTO.from(
                launchProviders.map(LaunchProviderItemResponseDTO::from)));
    }

    @GetMapping("/{id}/operators")
    @Operation(summary = "Listar operadores do Launch Provider", description = "Retorna uma página com os operadores vinculados ao Launch Provider identificado pelo ID.")
    public ResponseEntity<PageResponseDTO<OperatorItemResponseDTO>> findAllLaunchProviderOperators(
            @PathVariable Long id,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int items,
            @RequestParam(defaultValue = "operatorId") OperatorSortField sortBy,
            @RequestParam(defaultValue = "asc") String direction,
            @AuthenticationPrincipal JwtUserData authUser) {

        Page<Operator> operators = operatorService.findAllByLaunchProviderId(
                id, page, items, sortBy, direction, authUser);
        return ResponseEntity.ok(PageResponseDTO.from(
                operators.map(OperatorItemResponseDTO::from)));
    }

    @GetMapping("/{id}/satellites")
    @Operation(summary = "Listar satélites do Launch Provider", description = "Retorna uma página com os satélites associados ao Launch Provider informado pelo ID.")
    public ResponseEntity<PageResponseDTO<SatelliteItemResponseDTO>> findAllLaunchProviderSatellites(
            @PathVariable Long id,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int items,
            @RequestParam(defaultValue = "operatorId") SatelliteSortField sortBy,
            @RequestParam(defaultValue = "asc") String direction,
            @AuthenticationPrincipal JwtUserData authUser) {

        Page<Satellite> satellites = satelliteService.findAllByLaunchProviderId(
                id, page, items, sortBy, direction, authUser);
        return ResponseEntity.ok(PageResponseDTO.from(
                satellites.map(SatelliteItemResponseDTO::from)));
    }

    @PostMapping
    @Operation(summary = "Criar um novo Launch Provider", description = "Recebe os dados de um Launch Provider e o cria no sistema.")
    public ResponseEntity<LaunchProviderResponseDTO> save(
            @Valid @RequestBody CreateLaunchProviderDTO dto) {

        LaunchProvider newLaunchProvider = launchProviderService.create(CreateLaunchProviderDTO.toEntity(dto));
        return new ResponseEntity<>(LaunchProviderResponseDTO.from(newLaunchProvider), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar um Launch Provider por ID", description = "Recebe os dados atualizados de um Launch Provider e o ID do Launch Provider a ser atualizado, e realiza a atualização no sistema.")
    public ResponseEntity<LaunchProviderResponseDTO> updateById(
            @PathVariable Long id,
            @Valid @RequestBody CreateLaunchProviderDTO dto,
            @AuthenticationPrincipal JwtUserData authUser) {

        LaunchProvider updated = launchProviderService.updateById(id, CreateLaunchProviderDTO.toEntity(dto),
                authUser);
        return ResponseEntity.ok(LaunchProviderResponseDTO.from(updated));
    }

    @PatchMapping("/{id}/password")
    @Operation(summary = "Atualizar a senha por ID", description = "Recebe o ID do Launch Provider e atualiza a senha.")
    public ResponseEntity<Void> updatePasswordById(
            @PathVariable Long id,
            @Valid @RequestBody ChangePasswordDTO dto,
            @AuthenticationPrincipal JwtUserData authUser) {

        launchProviderService.updatePasswordById(id, dto.getCurrentPassword(), dto.getNewPassword(), authUser);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar um Launch Provider por ID", description = "Recebe o ID de um Launch Provider e o deleta do sistema.")
    public ResponseEntity<Void> deleteById(
            @PathVariable Long id,
            @AuthenticationPrincipal JwtUserData authUser) {

        launchProviderService.deleteById(id, authUser);
        return ResponseEntity.noContent().build();
    }

}
