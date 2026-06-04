package br.com.fiap.javaadv.VeloSpace.presentation.transferObjects.LaunchProvider;

import br.com.fiap.javaadv.VeloSpace.model.LaunchProvider;
import lombok.Builder;

@Builder
public record LaunchProviderItemResponseDTO(
        Long launchProviderId,
        String corporateName) {

    public static LaunchProviderItemResponseDTO from(
            LaunchProvider launchProvider) {

        if (launchProvider == null) {
            return null;
        }

        return LaunchProviderItemResponseDTO.builder()
                .launchProviderId(launchProvider.getLaunchProviderId())
                .corporateName(launchProvider.getCorporateName())
                .build();
    }

}
