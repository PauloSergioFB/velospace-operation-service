package br.com.fiap.javaadv.VeloSpace.model;

import java.util.Objects;

import br.com.fiap.javaadv.VeloSpace.infrastructure.enums.Role;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "vs_user_role")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRole {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_role_id")
    private @Getter @Setter Long userRoleId;

    @Enumerated(EnumType.STRING)
    @Column(name = "code", nullable = false, unique = true, length = 55)
    private @Getter @Setter Role code;

    @Column(name = "description", nullable = false, unique = true, length = 55)
    private @Getter @Setter String description;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass())
            return false;
        UserRole that = (UserRole) o;
        return Objects.equals(userRoleId, that.getUserRoleId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(userRoleId);
    }

}
