package co.edu.unicauca.barbersoftwareback.fachadaServices.services;


import java.util.Collection;
import java.util.List;
import java.util.Optional;

import co.edu.unicauca.barbersoftwareback.capaAccesoDatos.models.CategoriaEntity;
import co.edu.unicauca.barbersoftwareback.capaAccesoDatos.repositories.CategoriaRepository;
import co.edu.unicauca.barbersoftwareback.fachadaServices.DTO.CategoriaDTORespuesta;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.stereotype.Service;

@Service //El objeto creado se almacena en el contenedor de Spring
public class CategoriaServiceImpl implements ICategoriaService{

    private CategoriaRepository servicioAccesoBaseDatos;
    private ModelMapper modelMapper;

    //El contructor inyecta los objetos que se encuentran en el contenedor de Spring
    public CategoriaServiceImpl(CategoriaRepository servicioAccesoBaseDatos,  ModelMapper modelMapper)
    {
        this.servicioAccesoBaseDatos=servicioAccesoBaseDatos;
        this.modelMapper=modelMapper;
    }

    @Override
    public List<CategoriaDTORespuesta> findAll() {
        List<CategoriaDTORespuesta> listaRetornar;
        Optional<Collection<CategoriaEntity>> CategoriaEntityOpt = this.servicioAccesoBaseDatos.findAll();

        // Si el Optional está vacío, devolvemos una lista vacía
        if (CategoriaEntityOpt.isEmpty()) {
            listaRetornar=List.of(); // Retorna una lista inmutable vacía
        }
        else{
            // Convertimos la colección a una lista y la mapeamos a ClienteDTO
            Collection<CategoriaEntity> categorias = CategoriaEntityOpt.get();
            listaRetornar= this.modelMapper.map(categorias, new TypeToken<List<CategoriaDTORespuesta>>() {}.getType());

        }

        return listaRetornar;
    }

}