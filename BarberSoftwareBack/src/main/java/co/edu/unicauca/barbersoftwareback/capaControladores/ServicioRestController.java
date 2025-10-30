package co.edu.unicauca.barbersoftwareback.capaControladores;

import java.util.List;

import co.edu.unicauca.barbersoftwareback.fachadaServices.DTO.ServicioDTOPeticion;
import co.edu.unicauca.barbersoftwareback.fachadaServices.DTO.ServicioDTORespuesta;
import co.edu.unicauca.barbersoftwareback.fachadaServices.services.IServicioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:4200", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE}
)
public class ServicioRestController {

    @Autowired
    private IServicioService servicioService;

    @GetMapping("/servicios")
    public List<ServicioDTORespuesta> listarServicios() {
        return servicioService.findAll();
    }

    @GetMapping("/servicios/{id}")
    public ServicioDTORespuesta consultarServicio(@PathVariable Integer id) {
        ServicioDTORespuesta objServicio = null;
        objServicio = servicioService.findById(id);
        return objServicio;
    }

    @PostMapping("/servicios")
    public ServicioDTORespuesta crearServicio(@RequestBody ServicioDTOPeticion servicio) {
        ServicioDTORespuesta objServicio = null;
        objServicio = servicioService.save(servicio);
        return objServicio;
    }

    @PutMapping("/servicios/{id}")
    public ServicioDTORespuesta actualizarServicio(@RequestBody ServicioDTOPeticion servicio, @PathVariable Integer id) {
        ServicioDTORespuesta objServicio = null;
        ServicioDTORespuesta servicioActual = servicioService.findById(id);
        if (servicioActual != null) {
            objServicio = servicioService.update(id, servicio);
        }
        return objServicio;
    }

    @DeleteMapping("/servicios/{id}")
    public Boolean eliminarServicio(@PathVariable Integer id) {
        Boolean bandera = false;
        ServicioDTORespuesta servicioActual = servicioService.findById(id);
        if (servicioActual != null) {
            bandera = servicioService.delete(id);
        }
        return bandera;
    }

    @GetMapping("/servicios/categoria/{idCategoria}")
    public List<ServicioDTORespuesta> listarServiciosPorCategoria(@PathVariable Integer idCategoria) {
        return servicioService.findByCategoria(idCategoria);
    }

    @GetMapping("/servicios/cliente")
    public List<ServicioDTORespuesta> listarServiciosActivos() {
        return servicioService.findAllClient();
    }

    @GetMapping("/servicios/cliente/categoria/{idCategoria}")
    public List<ServicioDTORespuesta> listarServiciosActivosPorCategoria(@PathVariable Integer idCategoria) {
        return servicioService.findByCategoriaClient(idCategoria);
    }
}
