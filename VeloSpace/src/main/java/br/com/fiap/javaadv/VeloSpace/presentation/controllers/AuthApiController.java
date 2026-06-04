package br.com.fiap.javaadv.VeloSpace.presentation.controllers;

import java.util.Optional;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import br.com.fiap.javaadv.VeloSpace.infrastructure.security.JwtHelper;
import br.com.fiap.javaadv.VeloSpace.infrastructure.security.JwtUserData;
import br.com.fiap.javaadv.VeloSpace.model.UserAccount;
import br.com.fiap.javaadv.VeloSpace.presentation.transferObjects.Auth.AuthRequestDTO;
import br.com.fiap.javaadv.VeloSpace.presentation.transferObjects.Auth.AuthResponseDTO;
import br.com.fiap.javaadv.VeloSpace.presentation.transferObjects.Auth.RefreshTokenRequestDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
@Tag(name = "Auth API", description = "Endpoints para autenticação e gerenciamento de tokens")
public class AuthApiController {

    private final AuthenticationManager authenticationManager;
    private final JwtHelper jwtHelper;

    @PostMapping
    @Operation(summary = "Autenticar usuário e gerar tokens", description = "Recebe email e senha, autentica o usuário e retorna um token JWT e um refresh token.")
    public ResponseEntity<AuthResponseDTO> login(@Valid @RequestBody AuthRequestDTO authRequestDTO) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authRequestDTO.email(),
                        authRequestDTO.password()));

        UserAccount user = (UserAccount) authentication.getPrincipal();
        String token = jwtHelper.generateToken(user);
        String refreshToken = jwtHelper.generateRefreshToken(user);

        return ResponseEntity.ok(new AuthResponseDTO(token, refreshToken));
    }

    @PostMapping("/refresh")
    @Operation(summary = "Gerar novo token", description = "Recebe um refresh token válido e retorna um novo token JWT e um novo refresh token.")
    public ResponseEntity<AuthResponseDTO> refreshToken(@RequestBody RefreshTokenRequestDTO refreshTokenRequestDTO) {

        Optional<JwtUserData> jwtData = jwtHelper.validateToken(
                refreshTokenRequestDTO.refreshToken());

        if (jwtData.isPresent()) {
            JwtUserData data = jwtData.get();
            UserAccount user = UserAccount.builder()
                    .userAccountId(data.userId())
                    .email(data.email())
                    .build();

            String newToken = jwtHelper.generateToken(user);
            String newRefreshToken = jwtHelper.generateRefreshToken(user);

            return ResponseEntity.ok(new AuthResponseDTO(newToken, newRefreshToken));
        }

        throw new ResponseStatusException(
                HttpStatus.UNAUTHORIZED, "Refresh Token inválido ou expirado");
    }

}
