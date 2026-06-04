package br.com.fiap.javaadv.VeloSpace.presentation.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import br.com.fiap.javaadv.VeloSpace.infrastructure.security.JwtUserData;
import br.com.fiap.javaadv.VeloSpace.model.Operator;
import br.com.fiap.javaadv.VeloSpace.presentation.transferObjects.Operator.ApprovalOperatorDTO;
import br.com.fiap.javaadv.VeloSpace.presentation.transferObjects.Operator.CreateOperatorDTO;
import br.com.fiap.javaadv.VeloSpace.presentation.transferObjects.Operator.OperatorResponseDTO;
import br.com.fiap.javaadv.VeloSpace.presentation.transferObjects.UserAccount.ChangePasswordDTO;
import br.com.fiap.javaadv.VeloSpace.service.Operator.OperatorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/operators")
@Tag(name = "Operator API", description = "Endpoints para gerenciamento de Operators")
public class OperatorApiController {

    private final OperatorService<Operator, Long> operatorService;

    @GetMapping("/me")
    @Operation(summary = "Buscar meu Operator", description = "Retorna os dados do Operator associado ao usuário autenticado.")
    public ResponseEntity<OperatorResponseDTO> findByMe(
            @PathVariable Long id,
            @AuthenticationPrincipal JwtUserData authUser) {

        Operator operator = operatorService.findById(authUser.userId(), authUser);
        return ResponseEntity.ok(OperatorResponseDTO.from(operator));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar Operator por ID", description = "Retorna os dados de um Operator específico, identificado pelo seu ID.")
    public ResponseEntity<OperatorResponseDTO> findById(
            @PathVariable Long id,
            @AuthenticationPrincipal JwtUserData authUser) {

        Operator operator = operatorService.findById(id, authUser);
        return ResponseEntity.ok(OperatorResponseDTO.from(operator));
    }

    @PostMapping
    @Operation(summary = "Criar um novo Operator", description = "Recebe os dados de um Operator e o cria no sistema.")
    public ResponseEntity<OperatorResponseDTO> save(
            @Valid @RequestBody CreateOperatorDTO dto) {

        Operator newOperator = operatorService.create(CreateOperatorDTO.toEntity(dto));
        return new ResponseEntity<>(OperatorResponseDTO.from(newOperator), HttpStatus.CREATED);
    }

    @PostMapping("/{id}/approval")
    @Operation(summary = "Aprovar ou rejeitar um Operator", description = "Recebe o ID de um Operator e os dados de aprovação, e atualiza o status de aprovação do Operator no sistema.")
    public ResponseEntity<Void> approval(
            @PathVariable Long id,
            @Valid @RequestBody ApprovalOperatorDTO dto,
            @AuthenticationPrincipal JwtUserData authUser) {

        operatorService.approval(id, dto.approval(), authUser);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/reapply")
    @Operation(summary = "Reaplicar um Operator", description = "Recebe o ID de um Operator e reaplica o processo de aprovação, permitindo que um Operator rejeitado seja avaliado novamente.")
    public ResponseEntity<Void> reapply(
            @PathVariable Long id,
            @AuthenticationPrincipal JwtUserData authUser) {

        operatorService.reapply(id, authUser);
        return ResponseEntity.noContent().build();
    };

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar um Operator por ID", description = "Recebe os dados atualizados de um Operator e o ID do Operator a ser atualizado, e realiza a atualização no sistema.")
    public ResponseEntity<OperatorResponseDTO> updateById(
            @PathVariable Long id,
            @Valid @RequestBody CreateOperatorDTO dto,
            @AuthenticationPrincipal JwtUserData authUser) {

        Operator updated = operatorService.updateById(id, CreateOperatorDTO.toEntity(dto), authUser);
        return ResponseEntity.ok(OperatorResponseDTO.from(updated));
    }

    @PatchMapping("/{id}/password")
    @Operation(summary = "Atualizar senha do Operator", description = "Atualiza a senha do Operator identificado pelo ID.")
    public ResponseEntity<Void> updatePasswordById(
            @PathVariable Long id,
            @Valid @RequestBody ChangePasswordDTO dto,
            @AuthenticationPrincipal JwtUserData authUser) {

        operatorService.updatePasswordById(id, dto.getCurrentPassword(), dto.getNewPassword(), authUser);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar um Operator por ID", description = "Recebe o ID de um Operator e o deleta do sistema.")
    public ResponseEntity<Void> deleteById(
            @PathVariable Long id,
            @AuthenticationPrincipal JwtUserData authUser) {

        operatorService.deleteById(id, authUser);
        return ResponseEntity.noContent().build();
    }

}
