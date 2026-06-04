package br.com.fiap.javaadv.VeloSpace.presentation.transferObjects.Shipper;

import br.com.fiap.javaadv.VeloSpace.infrastructure.enums.ShipperType;
import br.com.fiap.javaadv.VeloSpace.model.Shipper;
import br.com.fiap.javaadv.VeloSpace.presentation.transferObjects.UserAccount.UserAccountResponseDTO;
import lombok.Builder;

@Builder
public record ShipperResponseDTO(
        Long shipperId,
        ShipperType type,
        String shipperDocument,
        String name,
        UserAccountResponseDTO userAccount) {

    public static ShipperResponseDTO from(Shipper shipper) {
        if (shipper == null)
            return null;

        return ShipperResponseDTO.builder()
                .shipperId(shipper.getShipperId())
                .type(shipper.getType())
                .shipperDocument(shipper.getShipperDocument())
                .name(shipper.getName())
                .userAccount(UserAccountResponseDTO.from(shipper.getUserAccount()))
                .build();
    }

}
