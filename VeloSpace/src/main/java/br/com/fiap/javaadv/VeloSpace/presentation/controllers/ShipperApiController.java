package br.com.fiap.javaadv.VeloSpace.presentation.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.data.domain.Page;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import br.com.fiap.javaadv.VeloSpace.infrastructure.enums.SatelliteSortField;
import br.com.fiap.javaadv.VeloSpace.infrastructure.security.JwtUserData;
import br.com.fiap.javaadv.VeloSpace.model.Satellite;
import br.com.fiap.javaadv.VeloSpace.model.Shipper;
import br.com.fiap.javaadv.VeloSpace.presentation.transferObjects.PageResponseDTO;
import br.com.fiap.javaadv.VeloSpace.presentation.transferObjects.Satellite.SatelliteItemResponseDTO;
import br.com.fiap.javaadv.VeloSpace.presentation.transferObjects.Shipper.CreateShipperDTO;
import br.com.fiap.javaadv.VeloSpace.presentation.transferObjects.Shipper.ShipperResponseDTO;
import br.com.fiap.javaadv.VeloSpace.presentation.transferObjects.UserAccount.ChangePasswordDTO;
import br.com.fiap.javaadv.VeloSpace.service.Satellite.SatelliteService;
import br.com.fiap.javaadv.VeloSpace.service.Shipper.ShipperService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/shippers")
@Tag(name = "Shipper API", description = "Endpoints para gerenciamento de Shippers")
public class ShipperApiController {

    private final ShipperService<Shipper, Long> shipperService;

    private final SatelliteService<Satellite, Long> satelliteService;

    @GetMapping("/me")
    @Operation(summary = "Buscar Shipper do usuário", description = "Retorna informações completas do Shipper vinculado ao usuário autenticado.")
    public ResponseEntity<ShipperResponseDTO> findByMe(
            @AuthenticationPrincipal JwtUserData authUser) {

        Shipper shipper = shipperService.findById(authUser.userId(), authUser);
        return ResponseEntity.ok(ShipperResponseDTO.from(shipper));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar Shipper por ID", description = "Retorna os dados de um Shipper específico, identificado pelo seu ID.")
    public ResponseEntity<ShipperResponseDTO> findById(
            @PathVariable Long id,
            @AuthenticationPrincipal JwtUserData authUser) {

        Shipper shipper = shipperService.findById(id, authUser);
        return ResponseEntity.ok(ShipperResponseDTO.from(shipper));
    }

    @GetMapping("/hateoas/{id}")
    @Operation(summary = "Buscar Shipper com HATEOAS", description = "Retorna o Shipper por ID com links HATEOAS. Respostas: 200 OK (dados e links), 401 Não autenticado, 403 Acesso negado, 404 Shipper não encontrado.")
    public EntityModel<ShipperResponseDTO> findByIdHateoas(
            @PathVariable Long id,
            @AuthenticationPrincipal JwtUserData authUser) {

        Shipper shipper = shipperService.findById(id, authUser);
        return EntityModel.of(
                ShipperResponseDTO.from(shipper),

                linkTo(methodOn(ShipperApiController.class)
                        .findByIdHateoas(id, null)).withSelfRel(),

                linkTo(methodOn(ShipperApiController.class)
                        .findById(id, null)).withRel("shipper"),

                linkTo(methodOn(ShipperApiController.class)
                        .findByMe(null)).withRel("me"),

                linkTo(methodOn(ShipperApiController.class)
                        .findAllShipperSatellites(
                                id,
                                0, 10, SatelliteSortField.satelliteId, "asc",
                                null))
                        .withRel("satellites"),

                linkTo(methodOn(ShipperApiController.class)
                        .updateById(id, null, null)).withRel("update"),

                linkTo(methodOn(ShipperApiController.class)
                        .deleteById(id, null)).withRel("delete"));
    }

    @GetMapping("/{id}/satellites")
    @Operation(summary = "Listar satellites do Shipper", description = "Retorna uma página com os satellites associados ao Shipper identificado pelo ID.")
    public ResponseEntity<PageResponseDTO<SatelliteItemResponseDTO>> findAllShipperSatellites(
            @PathVariable Long id,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int items,
            @RequestParam(defaultValue = "operatorId") SatelliteSortField sortBy,
            @RequestParam(defaultValue = "asc") String direction,
            @AuthenticationPrincipal JwtUserData authUser) {

        Page<Satellite> satellites = satelliteService.findAllByShipperId(
                id, page, items, sortBy, direction, authUser);
        return ResponseEntity.ok(PageResponseDTO.from(
                satellites.map(SatelliteItemResponseDTO::from)));
    }

    @PostMapping
    @Operation(summary = "Criar um novo Shipper", description = "Recebe os dados de um Shipper e o cria no sistema.")
    public ResponseEntity<ShipperResponseDTO> save(
            @Valid @RequestBody CreateShipperDTO dto) {

        Shipper newShipper = shipperService.create(CreateShipperDTO.toEntity(dto));
        return new ResponseEntity<>(ShipperResponseDTO.from(newShipper), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar um Shipper por ID", description = "Recebe os dados atualizados de um Shipper e o ID do Shipper a ser atualizado, e realiza a atualização no sistema.")
    public ResponseEntity<ShipperResponseDTO> updateById(
            @PathVariable Long id,
            @Valid @RequestBody CreateShipperDTO dto,
            @AuthenticationPrincipal JwtUserData authUser) {

        Shipper updated = shipperService.updateById(id, CreateShipperDTO.toEntity(dto), authUser);
        return ResponseEntity.ok(ShipperResponseDTO.from(updated));
    }

    @PatchMapping("/{id}/password")
    @Operation(summary = "Atualizar senha do Shipper", description = "Atualiza a senha do Shipper identificado pelo ID.")
    public ResponseEntity<Void> updatePasswordById(
            @PathVariable Long id,
            @Valid @RequestBody ChangePasswordDTO dto,
            @AuthenticationPrincipal JwtUserData authUser) {

        shipperService.updatePasswordById(id, dto.getCurrentPassword(), dto.getNewPassword(), authUser);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar um Shipper por ID", description = "Recebe o ID de um Shipper e o deleta do sistema.")
    public ResponseEntity<Void> deleteById(
            @PathVariable Long id,
            @AuthenticationPrincipal JwtUserData authUser) {

        shipperService.deleteById(id, authUser);
        return ResponseEntity.noContent().build();
    }

}
