package br.com.fiap.javaadv.VeloSpace.infrastructure.mongo.document;

import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Document(collection = "operator_refs")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OperatorRef {

    @Id
    private String id;

    private Long operatorId;

    private Long launchProviderId;

    private Long userAccountId;

    private String operatorStatusCode;

}
