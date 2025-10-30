package co.edu.unicauca.barbersoftwareback.capaAccesoDatos.models;

import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ServicioEntity {
    private Integer id;
    private String nombre;
    private String descripcion;
    private Double precio;
    private Integer duracionMinutos;
    private Date fechaCreacion;
    private String imagenBase64;
    private String estado;

    private CategoriaEntity objCategoria;

    public ServicioEntity() {
    }
}