package co.edu.unicauca.barbersoftwareback.fachadaServices.services;

import co.edu.unicauca.barbersoftwareback.capaAccesoDatos.models.ServicioEntity;
import co.edu.unicauca.barbersoftwareback.capaAccesoDatos.repositories.ServicioRepository;
import co.edu.unicauca.barbersoftwareback.fachadaServices.DTO.ServicioDTOPeticion;
import co.edu.unicauca.barbersoftwareback.fachadaServices.DTO.ServicioDTORespuesta;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service // El objeto creado se almacena en el contenedor de Spring
public class ServicioServiceImpl implements IServicioService {

    private final ServicioRepository servicioAccesoBaseDatos;
    private final ModelMapper modelMapper;

    // Constructor con inyección de dependencias
    public ServicioServiceImpl(ServicioRepository servicioAccesoBaseDatos, ModelMapper modelMapper) {
        this.servicioAccesoBaseDatos = servicioAccesoBaseDatos;
        this.modelMapper = modelMapper;
    }

    @Override
    public List<ServicioDTORespuesta> findAll() {
        List<ServicioDTORespuesta> listaRetornar;
        Optional<Collection<ServicioEntity>> serviciosEntityOpt = this.servicioAccesoBaseDatos.findAll();

        if (serviciosEntityOpt.isEmpty()) {
            listaRetornar = List.of();
        } else {
            Collection<ServicioEntity> serviciosEntity = serviciosEntityOpt.get();
            listaRetornar = this.modelMapper.map(serviciosEntity, new TypeToken<List<ServicioDTORespuesta>>() {}.getType());
        }

        return listaRetornar;
    }

    @Override
    public ServicioDTORespuesta findById(Integer id) {
        ServicioDTORespuesta servicioRetornar = null;
        Optional<ServicioEntity> optionalServicio = this.servicioAccesoBaseDatos.findById(id);

        if (optionalServicio.isPresent()) {
            ServicioEntity servicioEntity = optionalServicio.get();
            servicioRetornar = this.modelMapper.map(servicioEntity, ServicioDTORespuesta.class);
        }

        return servicioRetornar;
    }

    @Override
    public ServicioDTORespuesta save(ServicioDTOPeticion servicio) {
        ServicioEntity servicioEntity = this.modelMapper.map(servicio, ServicioEntity.class);

        // Asignar fecha de creación automáticamente
        servicioEntity.setFechaCreacion(new Date());

        // 🔹 Si no se envía estado, se establece por defecto "Activo"
        if (servicioEntity.getEstado() == null || servicioEntity.getEstado().isBlank()) {
            servicioEntity.setEstado("Activo");
        }

        // Registrar servicio en BD
        Optional<ServicioEntity> objServicioEntity = this.servicioAccesoBaseDatos.save(servicioEntity);

        if (objServicioEntity.isEmpty()) {
            return null;
        }

        // Convertir a DTO de respuesta
        return this.modelMapper.map(objServicioEntity.get(), ServicioDTORespuesta.class);
    }


    @Override
    public ServicioDTORespuesta update(Integer id, ServicioDTOPeticion servicio) {
        ServicioEntity servicioActualizado = null;

        Optional<ServicioEntity> servicioEntityOp = this.servicioAccesoBaseDatos.findById(id);

        if (servicioEntityOp.isPresent()) {
            ServicioEntity objServicioDatosNuevos = servicioEntityOp.get();

            objServicioDatosNuevos.setNombre(servicio.getNombre());
            objServicioDatosNuevos.setDescripcion(servicio.getDescripcion());
            objServicioDatosNuevos.setPrecio(servicio.getPrecio());
            objServicioDatosNuevos.setDuracionMinutos(servicio.getDuracionMinutos());

            // 🔹 Actualizar imagen si viene en la petición
            if (servicio.getImagenBase64() != null) {
                objServicioDatosNuevos.setImagenBase64(servicio.getImagenBase64());
            }

            // 🔹 Actualizar estado si viene en la petición
            if (servicio.getEstado() != null && !servicio.getEstado().isBlank()) {
                objServicioDatosNuevos.setEstado(servicio.getEstado());
            }

            // Actualizar la categoría
            if (servicio.getObjCategoria() != null) {
                objServicioDatosNuevos.getObjCategoria().setId(servicio.getObjCategoria().getId());
                objServicioDatosNuevos.getObjCategoria().setNombre("");
            }

            Optional<ServicioEntity> optionalServicio = this.servicioAccesoBaseDatos.update(id, objServicioDatosNuevos);
            if (optionalServicio.isPresent()) {
                servicioActualizado = optionalServicio.get();
            }
        }

        return servicioActualizado == null ? null : this.modelMapper.map(servicioActualizado, ServicioDTORespuesta.class);
    }


    @Override
    public boolean delete(Integer id) {
        return this.servicioAccesoBaseDatos.delete(id);
    }

    @Override
    public List<ServicioDTORespuesta> findByCategoria(Integer idCategoria) {
        List<ServicioDTORespuesta> listaRetornar;
        Optional<Collection<ServicioEntity>> serviciosEntityOpt = this.servicioAccesoBaseDatos.findByCategoria(idCategoria);

        if (serviciosEntityOpt.isEmpty()) {
            listaRetornar = List.of();
        } else {
            Collection<ServicioEntity> serviciosEntity = serviciosEntityOpt.get();
            listaRetornar = this.modelMapper.map(serviciosEntity, new TypeToken<List<ServicioDTORespuesta>>() {}.getType());
        }

        return listaRetornar;
    }

    @Override
    public List<ServicioDTORespuesta> findAllClient() {
        List<ServicioDTORespuesta> listaRetornar;
        Optional<Collection<ServicioEntity>> serviciosEntityOpt = this.servicioAccesoBaseDatos.findAllClient();

        if (serviciosEntityOpt.isEmpty()) {
            listaRetornar = List.of();
        } else {
            Collection<ServicioEntity> serviciosEntity = serviciosEntityOpt.get();
            listaRetornar = this.modelMapper.map(serviciosEntity, new TypeToken<List<ServicioDTORespuesta>>() {}.getType());
        }

        return listaRetornar;
    }

    @Override
    public List<ServicioDTORespuesta> findByCategoriaClient(Integer idCategoria) {
        List<ServicioDTORespuesta> listaRetornar;
        Optional<Collection<ServicioEntity>> serviciosEntityOpt = this.servicioAccesoBaseDatos.findByCategoriaClient(idCategoria);

        if (serviciosEntityOpt.isEmpty()) {
            listaRetornar = List.of();
        } else {
            Collection<ServicioEntity> serviciosEntity = serviciosEntityOpt.get();
            listaRetornar = this.modelMapper.map(serviciosEntity, new TypeToken<List<ServicioDTORespuesta>>() {}.getType());
        }

        return listaRetornar;
    }

}
