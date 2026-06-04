package br.com.fiap.javaadv.VeloSpace.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;

@Entity
@Table(name = "vs_satellite_priority")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SatellitePriority {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "satellite_priority_id")
    private @Getter @Setter Long satellitePriorityId;

    @Column(name = "priority_level", nullable = false)
    private @Getter @Setter Long priority_level;

    @Column(name = "description", nullable = false, length = 55)
    private @Getter @Setter String description;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass())
            return false;
        SatellitePriority that = (SatellitePriority) o;
        return Objects.equals(satellitePriorityId, that.getSatellitePriorityId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(satellitePriorityId);
    }

}
