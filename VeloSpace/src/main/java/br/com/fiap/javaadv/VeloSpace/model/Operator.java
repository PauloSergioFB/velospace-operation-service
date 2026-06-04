package br.com.fiap.javaadv.VeloSpace.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@Entity
@Table(name = "vs_operator")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Operator {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "operator_id")
    private @Getter @Setter Long operatorId;

    @ManyToOne
    @JoinColumn(name = "launch_provider_id", nullable = false)
    private @Getter @Setter LaunchProvider launchProvider;

    @Column(name = "cpf", nullable = false, unique = true)
    private @Getter @Setter String cpf;

    @Column(name = "name", nullable = false, length = 255)
    private @Getter @Setter String name;

    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "user_account_id", nullable = false, unique = true)
    private @Getter @Setter UserAccount userAccount;

    @ManyToOne
    @JoinColumn(name = "operator_status_id", nullable = false)
    private @Getter @Setter OperatorStatus operatorStatus;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass())
            return false;
        Operator that = (Operator) o;
        return Objects.equals(operatorId, that.getOperatorId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(operatorId);
    }

}
