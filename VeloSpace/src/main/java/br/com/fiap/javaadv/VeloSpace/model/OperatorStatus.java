package br.com.fiap.javaadv.VeloSpace.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@Entity
@Table(name = "vs_operator_status")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OperatorStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "operator_status_id")
    private @Getter @Setter Long operatorStatusId;

    @Column(name = "code", nullable = false, unique = true, length = 55)
    private @Getter @Setter String code;

    @Column(name = "description", nullable = false, unique = true, length = 55)
    private @Getter @Setter String description;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass())
            return false;
        OperatorStatus that = (OperatorStatus) o;
        return Objects.equals(operatorStatusId, that.getOperatorStatusId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(operatorStatusId);
    }

}
