package br.com.fiap.javaadv.VeloSpace.presentation.transferObjects.LaunchProvider;

import br.com.fiap.javaadv.VeloSpace.model.LaunchProvider;
import br.com.fiap.javaadv.VeloSpace.presentation.transferObjects.UserAccount.UserAccountResponseDTO;
import lombok.Builder;

@Builder
public record LaunchProviderResponseDTO(
        Long launchProviderId,
        String cnpj,
        String corporateName,
        UserAccountResponseDTO userAccount) {

    public static LaunchProviderResponseDTO from(
            LaunchProvider launchProvider) {

        if (launchProvider == null) {
            return null;
        }

        return LaunchProviderResponseDTO.builder()
                .launchProviderId(launchProvider.getLaunchProviderId())
                .cnpj(launchProvider.getCnpj())
                .corporateName(launchProvider.getCorporateName())
                .userAccount(UserAccountResponseDTO.from(launchProvider.getUserAccount()))
                .build();
    }

}
