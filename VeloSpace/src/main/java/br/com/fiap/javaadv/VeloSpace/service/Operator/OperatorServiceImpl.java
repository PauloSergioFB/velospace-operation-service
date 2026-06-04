package br.com.fiap.javaadv.VeloSpace.service.Operator;

import br.com.fiap.javaadv.VeloSpace.infrastructure.enums.OperatorSortField;
import br.com.fiap.javaadv.VeloSpace.infrastructure.enums.Role;
import br.com.fiap.javaadv.VeloSpace.infrastructure.exceptions.BusinessRuleException;
import br.com.fiap.javaadv.VeloSpace.infrastructure.exceptions.FieldValidationException;
import br.com.fiap.javaadv.VeloSpace.infrastructure.exceptions.ForbiddenException;
import br.com.fiap.javaadv.VeloSpace.infrastructure.exceptions.NotFoundException;
import br.com.fiap.javaadv.VeloSpace.infrastructure.security.JwtUserData;
import br.com.fiap.javaadv.VeloSpace.model.LaunchProvider;
import br.com.fiap.javaadv.VeloSpace.model.Operator;
import br.com.fiap.javaadv.VeloSpace.model.OperatorStatus;
import br.com.fiap.javaadv.VeloSpace.model.UserAccount;
import br.com.fiap.javaadv.VeloSpace.model.UserRole;
import br.com.fiap.javaadv.VeloSpace.model.repository.OperatorRepository;
import br.com.fiap.javaadv.VeloSpace.service.LaunchProvider.LaunchProviderService;
import br.com.fiap.javaadv.VeloSpace.service.OperatorStatus.OperatorStatusService;
import br.com.fiap.javaadv.VeloSpace.service.UserRole.UserRoleService;
import br.com.fiap.javaadv.VeloSpace.service.UserValidation.UserValidationService;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class OperatorServiceImpl implements OperatorService<Operator, Long> {

    private final OperatorRepository operatorRepository;

    private final OperatorStatusService<OperatorStatus, Long> operatorStatusService;

    private final LaunchProviderService<LaunchProvider, Long> launchProviderService;

    private final UserValidationService userValidationService;

    private final UserRoleService<UserRole, Long> userRoleService;

    private final PasswordEncoder passwordEncoder;

    private void validateLaunchProviderOwner(JwtUserData authUser, Operator operator) {
        Long launchProviderUserAccountId = operator.getLaunchProvider().getUserAccount().getUserAccountId();

        if (!Objects.equals(authUser.userId(), launchProviderUserAccountId)) {
            throw new ForbiddenException(
                    "Você não possui permissão para acessar este operador.");
        }
    }

    private void validateOperatorOwner(JwtUserData authUser, Operator operator) {
        if (!Objects.equals(authUser.userId(), operator.getUserAccount().getUserAccountId())) {
            throw new ForbiddenException(
                    "Você não possui permissão para acessar este operador.");
        }
    }

    private void validateAccess(JwtUserData authUser, Operator operator) {
        if (authUser.role().equals(Role.LAUNCH_PROVIDER)) {
            validateLaunchProviderOwner(authUser, operator);
            return;
        }

        if (authUser.role().equals(Role.OPERATOR)) {
            validateOperatorOwner(authUser, operator);
            return;
        }

        throw new ForbiddenException(
                "Você não possui permissão para acessar este operador.");
    }

    private void validateCurrentStatus(
            Operator operator,
            String expectedStatusCode,
            String errorMessage) {

        String currentStatusCode = operator.getOperatorStatus().getCode();

        if (!currentStatusCode.equals(expectedStatusCode)) {
            throw new BusinessRuleException(errorMessage);
        }
    }

    private void changeStatus(Operator operator, String statusCode) {
        OperatorStatus status = operatorStatusService.getRequiredByCode(statusCode);
        operator.setOperatorStatus(status);
        operatorRepository.save(operator);
    }

    @Override
    public Operator findByIdOrThrow(Long id) {
        return operatorRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(
                        "Operador não encontrado."));
    }

    @Override
    public Operator findById(Long id, JwtUserData authUser) {
        Operator operator = findByIdOrThrow(id);
        validateAccess(authUser, operator);
        return operator;
    }

    @Override
    public Page<Operator> findAllByLaunchProviderId(
            Long id,
            int page,
            int items,
            OperatorSortField sortBy,
            String direction,
            JwtUserData authUser) {

        launchProviderService.findById(id, authUser);

        Sort sort = direction.equalsIgnoreCase("desc")
                ? Sort.by(sortBy.name()).descending()
                : Sort.by(sortBy.name()).ascending();

        return operatorRepository.findByLaunchProvider_LaunchProviderId(id, PageRequest.of(page, items, sort));
    }

    @Override
    public Operator create(Operator operator) {
        UserAccount userAccount = operator.getUserAccount();

        userValidationService.validUniqueEmail(userAccount.getEmail());

        operatorRepository.findByCpf(operator.getCpf())
                .ifPresent(other -> {
                    throw new FieldValidationException(
                            "cpf",
                            "Este CPF já está em uso.");
                });

        launchProviderService.findByIdOrThrow(operator.getLaunchProvider().getLaunchProviderId());

        OperatorStatus status = operatorStatusService.getRequiredByCode("PENDING_APPROVAL");
        operator.setOperatorStatus(status);

        UserRole operatorRole = userRoleService.getRequiredByCode(Role.OPERATOR);
        userAccount.setUserRole(operatorRole);

        userAccount.setHashedPassword(passwordEncoder.encode(userAccount.getHashedPassword()));
        operator.setUserAccount(userAccount);

        return operatorRepository.save(operator);
    }

    @Override
    public Operator updateById(Long id, Operator operator, JwtUserData authUser) {
        Operator existing = findByIdOrThrow(id);

        validateOperatorOwner(authUser, existing);

        UserAccount userAccount = operator.getUserAccount();
        UserAccount existingUserAccount = existing.getUserAccount();

        if (!passwordEncoder.matches(
                userAccount.getHashedPassword(),
                existingUserAccount.getHashedPassword())) {

            throw new FieldValidationException(
                    "password",
                    "Senha atual incorreta.");
        }

        if (!operator.getCpf().equals(existing.getCpf())) {
            operatorRepository.findByCpf(operator.getCpf())
                    .ifPresent(other -> {
                        throw new FieldValidationException(
                                "cpf",
                                "Este CPF já está em uso.");
                    });
        }

        if (!userAccount.getEmail().equals(existingUserAccount.getEmail())) {
            userValidationService.validUniqueEmail(userAccount.getEmail());
        }

        launchProviderService.findByIdOrThrow(
                operator.getLaunchProvider().getLaunchProviderId());

        existing.setName(operator.getName());
        existing.setCpf(operator.getCpf());
        existing.setLaunchProvider(operator.getLaunchProvider());

        existingUserAccount.setEmail(userAccount.getEmail());
        existingUserAccount.setPhone(userAccount.getPhone());

        return operatorRepository.save(existing);
    }

    @Override
    public void updatePasswordById(Long id, String currentPassword, String newPassword, JwtUserData authUser) {
        Operator existing = findByIdOrThrow(id);

        validateOperatorOwner(authUser, existing);

        UserAccount existingUserAccount = existing.getUserAccount();

        if (!passwordEncoder.matches(
                currentPassword,
                existingUserAccount.getHashedPassword())) {

            throw new FieldValidationException(
                    "current_password",
                    "Senha atual incorreta.");
        }

        existingUserAccount.setHashedPassword(passwordEncoder.encode(newPassword));
        operatorRepository.save(existing);
    }

    @Override
    public void deleteById(Long id, JwtUserData authUser) {
        Operator operator = findByIdOrThrow(id);
        validateOperatorOwner(authUser, operator);
        operatorRepository.delete(operator);
    }

    @Override
    public void approval(Long id, boolean approved, JwtUserData authUser) {
        Operator operator = findByIdOrThrow(id);

        validateLaunchProviderOwner(authUser, operator);

        validateCurrentStatus(
                operator,
                "PENDING_APPROVAL",
                "Só é possível aprovar ou reprovar um operador que está aguardando aprovação.");

        changeStatus(operator, approved ? "APPROVED" : "REJECTED");
    }

    @Override
    public void reapply(Long id, JwtUserData authUser) {
        Operator operator = findByIdOrThrow(id);

        validateOperatorOwner(authUser, operator);

        validateCurrentStatus(
                operator,
                "REJECTED",
                "Só é possível reaplicar um operador que está rejeitado.");

        changeStatus(operator, "PENDING_APPROVAL");
    }

}
