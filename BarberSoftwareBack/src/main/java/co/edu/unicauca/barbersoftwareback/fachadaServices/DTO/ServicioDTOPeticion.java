package co.edu.unicauca.barbersoftwareback.fachadaServices.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ServicioDTOPeticion {
    private String nombre;
    private String descripcion;
    private Double precio;
    private Integer duracionMinutos;

    private CategoriaDTOPeticion objCategoria;
}