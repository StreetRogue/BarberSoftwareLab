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

    // 🔹 Nuevo campo para enviar la imagen desde el frontend (Base64)
    private String imagenBase64;

    // 🔹 Estado del servicio: "Activo" o "Inactivo"
    private String estado;

    private CategoriaDTOPeticion objCategoria;
}
