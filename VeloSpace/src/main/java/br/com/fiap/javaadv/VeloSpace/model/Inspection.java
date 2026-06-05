package br.com.fiap.javaadv.VeloSpace.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Objects;

import br.com.fiap.javaadv.VeloSpace.infrastructure.enums.InspectionResult;

@Entity
@Table(name = "vs_inspection")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Inspection {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "inspection_id")
    private @Getter @Setter Long inspectionId;

    @ManyToOne
    @JoinColumn(name = "satellite_id", nullable = false)
    private @Getter @Setter Satellite satellite;

    @JoinColumn(name = "operator_id", nullable = false)
    private @Getter @Setter Long operatorId;

    @Column(name = "measured_height", nullable = false)
    private @Getter @Setter Integer measuredHeight;

    @Column(name = "measured_width", nullable = false)
    private @Getter @Setter Integer measuredWidth;

    @Column(name = "measured_length", nullable = false)
    private @Getter @Setter Integer measuredLength;

    @Column(name = "measured_weight", nullable = false)
    private @Getter @Setter Integer measuredWeight;

    @Enumerated(EnumType.STRING)
    @Column(name = "result", nullable = false, columnDefinition = "VARCHAR2(1)")
    private @Getter @Setter InspectionResult result;

    @Column(name = "inspection_date", nullable = false)
    private @Getter @Setter LocalDateTime inspectionDate;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass())
            return false;
        Inspection that = (Inspection) o;
        return Objects.equals(inspectionId, that.getInspectionId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(inspectionId);
    }

}
