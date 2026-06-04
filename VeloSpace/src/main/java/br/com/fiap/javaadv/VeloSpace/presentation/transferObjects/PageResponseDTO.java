package br.com.fiap.javaadv.VeloSpace.presentation.transferObjects;

import java.util.List;

import org.springframework.data.domain.Page;

import lombok.Builder;

@Builder
public record PageResponseDTO<T>(
        int page,
        int totalPages,
        long totalItems,
        List<T> items) {

    public static <T> PageResponseDTO<T> from(Page<T> page) {
        if (page == null) {
            return null;
        }

        return PageResponseDTO.<T>builder()
                .page(page.getNumber())
                .totalPages(page.getTotalPages())
                .totalItems(page.getTotalElements())
                .items(page.getContent())
                .build();
    }

}
