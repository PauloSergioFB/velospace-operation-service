package br.com.fiap.javaadv.VeloSpace.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@Entity
@Table(name = "vs_launch_provider")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LaunchProvider {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "launch_provider_id")
    private @Getter @Setter Long launchProviderId;

    @Column(name = "cnpj", nullable = false, unique = true, length = 14)
    private @Getter @Setter String cnpj;

    @Column(name = "corporate_name", nullable = false, length = 255)
    private @Getter @Setter String corporateName;

    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "user_account_id", nullable = false, unique = true)
    private @Getter @Setter UserAccount userAccount;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass())
            return false;
        LaunchProvider that = (LaunchProvider) o;
        return Objects.equals(launchProviderId, that.getLaunchProviderId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(launchProviderId);
    }

}
