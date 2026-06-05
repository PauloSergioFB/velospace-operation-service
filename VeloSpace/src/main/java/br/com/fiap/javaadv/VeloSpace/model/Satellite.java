package br.com.fiap.javaadv.VeloSpace.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;

@Entity
@Table(name = "vs_satellite")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Satellite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "satellite_id")
    private @Getter @Setter Long satelliteId;

    @JoinColumn(name = "shipper_id", nullable = false)
    private @Getter @Setter Long shipperId;

    @JoinColumn(name = "launch_provider_id", nullable = false)
    private @Getter @Setter Long launchProviderId;

    @Column(name = "name", nullable = false, length = 55)
    private @Getter @Setter String name;

    @Column(name = "height", nullable = false)
    private @Getter @Setter Integer height;

    @Column(name = "width", nullable = false)
    private @Getter @Setter Integer width;

    @Column(name = "length", nullable = false)
    private @Getter @Setter Integer length;

    @Column(name = "weight", nullable = false)
    private @Getter @Setter Integer weight;

    @Column(name = "launch_justification", nullable = false, length = 500)
    private @Getter @Setter String launchJustification;

    @Column(name = "tracking_code", length = 55)
    private @Getter @Setter String trackingCode;

    @ManyToOne
    @JoinColumn(name = "satellite_status_id", nullable = false)
    private @Getter @Setter SatelliteStatus satelliteStatus;

    @ManyToOne
    @JoinColumn(name = "satellite_priority_id")
    private @Getter @Setter SatellitePriority satellitePriority;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass())
            return false;
        Satellite that = (Satellite) o;
        return Objects.equals(satelliteId, that.getSatelliteId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(satelliteId);
    }

}
