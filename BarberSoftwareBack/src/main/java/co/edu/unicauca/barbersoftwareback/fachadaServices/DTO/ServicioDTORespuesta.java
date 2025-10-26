package co.edu.unicauca.barbersoftwareback.fachadaServices.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ServicioDTORespuesta {
    private Integer id;
    private String nombre;
    private String descripcion;
    private Double precio;
    private Integer duracionMinutos;
    private Date fechaCreacion;

    private CategoriaDTORespuesta objCategoria;
}
