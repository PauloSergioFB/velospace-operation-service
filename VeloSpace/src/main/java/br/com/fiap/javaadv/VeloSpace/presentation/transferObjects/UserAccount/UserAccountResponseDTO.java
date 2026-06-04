package br.com.fiap.javaadv.VeloSpace.presentation.transferObjects.UserAccount;

import br.com.fiap.javaadv.VeloSpace.model.UserAccount;
import lombok.Builder;

@Builder
public record UserAccountResponseDTO(
        Long userAccountId,
        String email,
        String phone) {

    public static UserAccountResponseDTO from(UserAccount userAccount) {
        if (userAccount == null)
            return null;

        return UserAccountResponseDTO.builder()
                .userAccountId(userAccount.getUserAccountId())
                .email(userAccount.getEmail())
                .phone(userAccount.getPhone())
                .build();
    }

}
