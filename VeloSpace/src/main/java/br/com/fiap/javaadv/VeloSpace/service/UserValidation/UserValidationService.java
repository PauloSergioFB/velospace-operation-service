package br.com.fiap.javaadv.VeloSpace.service.UserValidation;

import br.com.fiap.javaadv.VeloSpace.infrastructure.exceptions.FieldValidationException;
import br.com.fiap.javaadv.VeloSpace.model.repository.UserAccountRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserValidationService {

    private final UserAccountRepository userAccountRepository;

    public void validUniqueEmail(String email) {
        if (userAccountRepository.existsByEmail(email)) {
            throw new FieldValidationException("email", "Este e-mail já está em uso.");
        }
    }

}
