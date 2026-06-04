package br.com.fiap.javaadv.VeloSpace.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "vs_rocket")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Rocket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "rocket_id")
    private @Getter @Setter Long rocketId;

    @Column(name = "name", nullable = false, unique = true, length = 55)
    private @Getter @Setter String name;

    @Column(name = "capacity_height", nullable = false)
    private @Getter @Setter Integer capacityHeight;

    @Column(name = "capacity_width", nullable = false)
    private @Getter @Setter Integer capacityWidth;

    @Column(name = "capacity_length", nullable = false)
    private @Getter @Setter Integer capacityLength;

    @Column(name = "capacity_weight", nullable = false)
    private @Getter @Setter Integer capacityWeight;

    @Column(name = "launch_date")
    private @Getter @Setter LocalDateTime launchDate;

    @ManyToOne
    @JoinColumn(name = "rocket_status_id", nullable = false)
    private @Getter @Setter RocketStatus rocketStatus;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass())
            return false;
        Rocket that = (Rocket) o;
        return Objects.equals(rocketId, that.getRocketId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(rocketId);
    }

}
