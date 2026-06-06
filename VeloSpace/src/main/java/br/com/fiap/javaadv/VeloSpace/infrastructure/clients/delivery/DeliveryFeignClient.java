package br.com.fiap.javaadv.VeloSpace.infrastructure.clients.delivery;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import br.com.fiap.javaadv.VeloSpace.infrastructure.clients.delivery.transferObjects.TrackResponseDTO;

@FeignClient(name = "deliveryClient", url = "${DELIVERY_API_URL}")
public interface DeliveryFeignClient {

        @GetMapping("/track/{code}")
        TrackResponseDTO track(@PathVariable String code);

}
