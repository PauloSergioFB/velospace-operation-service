package br.com.fiap.javaadv.VeloSpace.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;

@Entity
@Table(name = "vs_satellite_status")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SatelliteStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "satellite_status_id")
    private @Getter @Setter Long satelliteStatusId;

    @Column(name = "code", nullable = false, unique = true, length = 55)
    private @Getter @Setter String code;

    @Column(name = "description", nullable = false, unique = true, length = 55)
    private @Getter @Setter String description;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass())
            return false;
        SatelliteStatus that = (SatelliteStatus) o;
        return Objects.equals(satelliteStatusId, that.getSatelliteStatusId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(satelliteStatusId);
    }

}
