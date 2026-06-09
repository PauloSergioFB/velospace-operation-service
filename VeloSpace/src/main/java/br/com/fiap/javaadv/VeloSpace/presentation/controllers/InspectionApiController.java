package br.com.fiap.javaadv.VeloSpace.presentation.controllers;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.fiap.javaadv.VeloSpace.infrastructure.security.JwtUserData;
import br.com.fiap.javaadv.VeloSpace.model.Inspection;
import br.com.fiap.javaadv.VeloSpace.presentation.transferObjects.Inspection.CreateInspectionDTO;
import br.com.fiap.javaadv.VeloSpace.presentation.transferObjects.Inspection.InspectionResponseDTO;
import br.com.fiap.javaadv.VeloSpace.service.Inspection.InspectionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("api/v1/inspections")
@RequiredArgsConstructor
@Tag(name = "Inspection API", description = "Endpoints para gerenciamento de Inspections")
public class InspectionApiController {

    private final InspectionService<Inspection, Long> inspectionService;

    @GetMapping("/{id}")
    @Operation(summary = "Buscar Inspection por ID", description = "Retorna os dados de uma Inspection específico, identificada pelo seu ID.")
    public ResponseEntity<EntityModel<InspectionResponseDTO>> findById(
            @PathVariable Long id,
            @AuthenticationPrincipal JwtUserData authUser) {

        Inspection inspection = inspectionService.findById(id, authUser);
        return ResponseEntity.ok(
                EntityModel.of(InspectionResponseDTO.from(inspection),
                        linkTo(methodOn(InspectionApiController.class)
                                .findById(id, authUser))
                                .withSelfRel(),
                        linkTo(methodOn(SatelliteApiController.class)
                                .findById(inspection.getSatellite().getSatelliteId(),
                                        authUser))
                                .withRel("satellite")));
    }

    @PostMapping
    @Operation(summary = "Criar uma nova Inspection", description = "Recebe os dados de uma Inspection e a cria no sistema.")
    public ResponseEntity<InspectionResponseDTO> save(
            @Valid @RequestBody CreateInspectionDTO dto,
            @AuthenticationPrincipal JwtUserData authUser) {

        Inspection newInspection = inspectionService.create(CreateInspectionDTO.toEntity(dto), authUser);
        return new ResponseEntity<>(InspectionResponseDTO.from(newInspection), HttpStatus.CREATED);
    }

}
