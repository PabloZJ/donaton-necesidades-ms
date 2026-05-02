package microservice.necesidades.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "necesidad", schema = "necesidades")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Necesidad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "tipo_recurso_id", nullable = false)
    private Integer tipoRecursoId;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal cantidad;

    @Column(columnDefinition = "TEXT")
    private String descripcion;

    @Column(nullable = false, precision = 9, scale = 6)
    private BigDecimal latitud;

    @Column(nullable = false, precision = 9, scale = 6)
    private BigDecimal longitud;

    @ManyToOne
    @JoinColumn(name = "estado_id", nullable = false)
    private EstadoNecesidad estado;

    @Column(name = "reportado_por_uid", nullable = false, length = 128)
    private String reportadoPorUid;

    @Column(name = "fecha_reporte", nullable = false)
    private LocalDateTime fechaReporte;
}