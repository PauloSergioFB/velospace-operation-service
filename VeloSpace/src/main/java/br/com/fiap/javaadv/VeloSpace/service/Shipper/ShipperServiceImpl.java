package br.com.fiap.javaadv.VeloSpace.service.Shipper;

import java.util.Objects;

import org.hibernate.validator.internal.constraintvalidators.hv.br.CPFValidator;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import br.com.caelum.stella.validation.CNPJValidator;
import br.com.fiap.javaadv.VeloSpace.infrastructure.enums.Role;
import br.com.fiap.javaadv.VeloSpace.infrastructure.enums.ShipperType;
import br.com.fiap.javaadv.VeloSpace.infrastructure.exceptions.FieldValidationException;
import br.com.fiap.javaadv.VeloSpace.infrastructure.exceptions.ForbiddenException;
import br.com.fiap.javaadv.VeloSpace.infrastructure.exceptions.NotFoundException;
import br.com.fiap.javaadv.VeloSpace.infrastructure.security.JwtUserData;
import br.com.fiap.javaadv.VeloSpace.model.Shipper;
import br.com.fiap.javaadv.VeloSpace.model.UserAccount;
import br.com.fiap.javaadv.VeloSpace.model.UserRole;
import br.com.fiap.javaadv.VeloSpace.model.repository.ShipperRepository;
import br.com.fiap.javaadv.VeloSpace.service.UserRole.UserRoleService;
import br.com.fiap.javaadv.VeloSpace.service.UserValidation.UserValidationService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ShipperServiceImpl implements ShipperService<Shipper, Long> {

    private final ShipperRepository shipperRepository;

    private final UserValidationService userValidationService;

    private final UserRoleService<UserRole, Long> userRoleService;

    private final PasswordEncoder passwordEncoder;

    private void validateShipperOwner(JwtUserData authUser, Shipper shipper) {
        if (!Objects.equals(authUser.userId(), shipper.getUserAccount().getUserAccountId())) {
            throw new ForbiddenException(
                    "Você não possui permissão para acessar este expedidor.");
        }
    }

    private void validateDocument(ShipperType type, String document) {
        if (type.equals(ShipperType.PF)) {
            CPFValidator cpfValidator = new CPFValidator();
            cpfValidator.initialize(null);

            if (!cpfValidator.isValid(document, null)) {
                throw new FieldValidationException(
                        "shipperDocument",
                        "CPF inválido.");
            }

            return;
        }

        if (type.equals(ShipperType.PJ)) {
            CNPJValidator cnpjValidator = new CNPJValidator();

            try {
                cnpjValidator.assertValid(document);
            } catch (Exception e) {
                throw new FieldValidationException(
                        "shipperDocument",
                        "CNPJ inválido.");
            }

            return;
        }

        throw new FieldValidationException(
                "type",
                "Tipo de expedidor inválido.");
    }

    private void validateUniqueDocument(String document) {
        shipperRepository.findByShipperDocument(document)
                .ifPresent(other -> {
                    throw new FieldValidationException(
                            "shipperDocument",
                            "Este documento já está em uso.");
                });
    }

    @Override
    public Shipper findByIdOrThrow(Long id) {
        return shipperRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(
                        "Expedidor não encontrado."));
    }

    @Override
    public Shipper findByUserAccountIdOrThrow(Long id) {
        return shipperRepository.findByUserAccount_UserAccountId(id)
                .orElseThrow(() -> new NotFoundException(
                        "Expedidor não encontrado."));
    }

    @Override
    public Shipper findById(Long id, JwtUserData authUser) {
        Shipper shipper = findByIdOrThrow(id);
        validateShipperOwner(authUser, shipper);
        return shipper;
    }

    @Override
    public Shipper findByUserAccountId(Long id, JwtUserData authUser) {
        Shipper shipper = findByUserAccountIdOrThrow(id);
        validateShipperOwner(authUser, shipper);
        return shipper;
    }

    @Override
    public Shipper create(Shipper shipper) {
        validateDocument(
                shipper.getType(),
                shipper.getShipperDocument());

        UserAccount userAccount = shipper.getUserAccount();

        userValidationService.validUniqueEmail(userAccount.getEmail());

        validateUniqueDocument(shipper.getShipperDocument());

        UserRole shipperRole = userRoleService.getRequiredByCode(Role.SHIPPER);
        userAccount.setUserRole(shipperRole);

        userAccount.setHashedPassword(passwordEncoder.encode(userAccount.getHashedPassword()));
        shipper.setUserAccount(userAccount);

        return shipperRepository.save(shipper);
    }

    @Override
    public Shipper updateById(Long id, Shipper shipper, JwtUserData authUser) {
        Shipper existing = findByIdOrThrow(id);

        validateShipperOwner(authUser, existing);

        UserAccount userAccount = shipper.getUserAccount();
        UserAccount existingUserAccount = existing.getUserAccount();

        if (!passwordEncoder.matches(
                userAccount.getHashedPassword(),
                existingUserAccount.getHashedPassword())) {

            throw new FieldValidationException(
                    "password",
                    "Senha atual incorreta.");
        }

        validateDocument(
                shipper.getType(),
                shipper.getShipperDocument());

        if (!shipper.getShipperDocument().equals(existing.getShipperDocument())) {
            validateUniqueDocument(shipper.getShipperDocument());
        }

        if (!userAccount.getEmail().equals(existingUserAccount.getEmail())) {
            userValidationService.validUniqueEmail(userAccount.getEmail());
        }

        existing.setName(shipper.getName());
        existing.setShipperDocument(shipper.getShipperDocument());
        existing.setType(shipper.getType());

        existingUserAccount.setEmail(userAccount.getEmail());
        existingUserAccount.setPhone(userAccount.getPhone());

        return shipperRepository.save(existing);
    }

    @Override
    public void updatePasswordById(Long id, String currentPassword, String newPassword, JwtUserData authUser) {
        Shipper existing = findByIdOrThrow(id);

        validateShipperOwner(authUser, existing);

        UserAccount existingUserAccount = existing.getUserAccount();

        if (!passwordEncoder.matches(
                currentPassword,
                existingUserAccount.getHashedPassword())) {

            throw new FieldValidationException(
                    "current_password",
                    "Senha atual incorreta.");
        }

        existingUserAccount.setHashedPassword(passwordEncoder.encode(newPassword));
        shipperRepository.save(existing);
    }

    @Override
    public void deleteById(Long id, JwtUserData authUser) {
        Shipper shipper = findByIdOrThrow(id);
        validateShipperOwner(authUser, shipper);
        shipperRepository.delete(shipper);
    }

}
