package co.edu.unicauca.barbersoftwareback.capaControladores;

import java.util.List;

import co.edu.unicauca.barbersoftwareback.fachadaServices.DTO.CategoriaDTORespuesta;
import co.edu.unicauca.barbersoftwareback.fachadaServices.services.ICategoriaService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:4200", methods = {RequestMethod.GET})
public class CategoriaRestController {

    @Autowired
    private ICategoriaService categoriaService;

    @GetMapping("/categorias")
    public List<CategoriaDTORespuesta> listarCategorias() {
        return categoriaService.findAll();
    }

}