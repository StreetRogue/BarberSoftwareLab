package co.edu.unicauca.barbersoftwareback.capaAccesoDatos.repositories;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import co.edu.unicauca.barbersoftwareback.capaAccesoDatos.models.CategoriaEntity;
import co.edu.unicauca.barbersoftwareback.capaAccesoDatos.models.ServicioEntity;
import co.edu.unicauca.barbersoftwareback.capaAccesoDatos.repositories.conexion.ConexionDB;

@Repository
public class ServicioRepository {

    private final ConexionDB conexionABaseDeDatos;

    public ServicioRepository() {
        conexionABaseDeDatos = new ConexionDB();
    }

    /**
     * Registra un servicio en la base de datos
     */
    public Optional<ServicioEntity> save(ServicioEntity objServicio) {
        System.out.println("Registrando servicio en base de datos");
        ServicioEntity objServicioAlmacenado = null;
        int resultado = -1;

        try {
            conexionABaseDeDatos.conectar();

            String consulta = "INSERT INTO servicios(nombre, descripcion, precio, duracionMinutos, fechaCreacion, idCategoria, imagenBase64, estado) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement sentencia = conexionABaseDeDatos.getConnection()
                    .prepareStatement(consulta, Statement.RETURN_GENERATED_KEYS);

            sentencia.setString(1, objServicio.getNombre());
            sentencia.setString(2, objServicio.getDescripcion());

            if (objServicio.getPrecio() != null) {
                sentencia.setDouble(3, objServicio.getPrecio());
            } else {
                sentencia.setNull(3, java.sql.Types.DOUBLE);
            }

            if (objServicio.getDuracionMinutos() != null) {
                sentencia.setInt(4, objServicio.getDuracionMinutos());
            } else {
                sentencia.setNull(4, java.sql.Types.INTEGER);
            }

            if (objServicio.getFechaCreacion() != null) {
                sentencia.setDate(5, new java.sql.Date(objServicio.getFechaCreacion().getTime()));
            } else {
                sentencia.setDate(5, new java.sql.Date(new java.util.Date().getTime()));
            }

            sentencia.setInt(6, objServicio.getObjCategoria().getId());

            // Imagen Base64
            sentencia.setString(7, objServicio.getImagenBase64());

            // Estado (Activo / Inactivo)
            if (objServicio.getEstado() != null) {
                sentencia.setString(8, objServicio.getEstado());
            } else {
                sentencia.setString(8, "Activo"); // valor por defecto
            }

            resultado = sentencia.executeUpdate();

            ResultSet generatedKeys = sentencia.getGeneratedKeys();
            if (generatedKeys.next()) {
                int idGenerado = generatedKeys.getInt(1);
                objServicio.setId(idGenerado);
                System.out.println("ID generado: " + idGenerado);
                if (resultado == 1) {
                    objServicioAlmacenado = this.findById(idGenerado).orElse(null);
                }
            } else {
                System.out.println("No se pudo obtener el ID generado.");
            }

            generatedKeys.close();
            sentencia.close();
            conexionABaseDeDatos.desconectar();

        } catch (SQLException e) {
            System.out.println("Error en la inserción: " + e.getMessage());
        }

        return objServicioAlmacenado == null ? Optional.empty() : Optional.of(objServicioAlmacenado);
    }

    /**
     * Lista todos los servicios
     */
    public Optional<Collection<ServicioEntity>> findAll() {
        System.out.println("Listando servicios de base de datos");
        Collection<ServicioEntity> servicios = new LinkedList<>();

        conexionABaseDeDatos.conectar();
        try {
            String consulta = "SELECT servicios.*, categorias.nombreCategoria "
                    + "FROM servicios JOIN categorias ON servicios.idCategoria = categorias.id";
            PreparedStatement sentencia = conexionABaseDeDatos.getConnection().prepareStatement(consulta);
            ResultSet res = sentencia.executeQuery();

            while (res.next()) {
                ServicioEntity objServicio = new ServicioEntity();
                objServicio.setId(res.getInt("id"));
                objServicio.setNombre(res.getString("nombre"));
                objServicio.setDescripcion(res.getString("descripcion"));

                double precioTemp = res.getDouble("precio");
                objServicio.setPrecio(res.wasNull() ? null : precioTemp);

                int duracionTemp = res.getInt("duracionMinutos");
                objServicio.setDuracionMinutos(res.wasNull() ? null : duracionTemp);

                objServicio.setFechaCreacion(res.getDate("fechaCreacion"));
                objServicio.setImagenBase64(res.getString("imagenBase64"));
                objServicio.setEstado(res.getString("estado"));

                objServicio.setObjCategoria(
                        new CategoriaEntity(res.getInt("idCategoria"), res.getString("nombreCategoria"))
                );

                servicios.add(objServicio);
            }

            sentencia.close();
            conexionABaseDeDatos.desconectar();

        } catch (SQLException e) {
            System.out.println("Error en la consulta: " + e.getMessage());
        }

        return servicios.isEmpty() ? Optional.empty() : Optional.of(servicios);
    }

    /**
     * Buscar por ID
     */
    public Optional<ServicioEntity> findById(Integer idServicio) {
        System.out.println("Consultando servicio de base de datos");
        ServicioEntity objServicio = null;

        conexionABaseDeDatos.conectar();
        try {
            String consulta = "SELECT servicios.*, categorias.nombreCategoria "
                    + "FROM servicios JOIN categorias ON servicios.idCategoria = categorias.id "
                    + "WHERE servicios.id = ?";
            PreparedStatement sentencia = conexionABaseDeDatos.getConnection().prepareStatement(consulta);
            sentencia.setInt(1, idServicio);
            ResultSet res = sentencia.executeQuery();

            while (res.next()) {
                objServicio = new ServicioEntity();
                objServicio.setId(res.getInt("id"));
                objServicio.setNombre(res.getString("nombre"));
                objServicio.setDescripcion(res.getString("descripcion"));

                double precioTemp = res.getDouble("precio");
                objServicio.setPrecio(res.wasNull() ? null : precioTemp);

                int duracionTemp = res.getInt("duracionMinutos");
                objServicio.setDuracionMinutos(res.wasNull() ? null : duracionTemp);

                objServicio.setFechaCreacion(res.getDate("fechaCreacion"));
                objServicio.setImagenBase64(res.getString("imagenBase64"));
                objServicio.setEstado(res.getString("estado"));

                objServicio.setObjCategoria(
                        new CategoriaEntity(res.getInt("idCategoria"), res.getString("nombreCategoria"))
                );
            }

            sentencia.close();
            conexionABaseDeDatos.desconectar();

        } catch (SQLException e) {
            System.out.println("Error en la consulta: " + e.getMessage());
        }

        return objServicio == null ? Optional.empty() : Optional.of(objServicio);
    }

    /**
     * Actualizar servicio
     */
    public Optional<ServicioEntity> update(Integer idServicio, ServicioEntity objServicio) {
        System.out.println("Actualizando servicio de base de datos");
        ServicioEntity objServicioActualizado = null;
        int resultado = -1;

        conexionABaseDeDatos.conectar();
        try {
            String consulta = "UPDATE servicios SET nombre=?, descripcion=?, precio=?, duracionMinutos=?, fechaCreacion=?, "
                    + "idCategoria=?, imagenBase64=?, estado=? WHERE id=?";
            PreparedStatement sentencia = conexionABaseDeDatos.getConnection().prepareStatement(consulta);

            sentencia.setString(1, objServicio.getNombre());
            sentencia.setString(2, objServicio.getDescripcion());

            if (objServicio.getPrecio() != null) {
                sentencia.setDouble(3, objServicio.getPrecio());
            } else {
                sentencia.setNull(3, java.sql.Types.DOUBLE);
            }

            if (objServicio.getDuracionMinutos() != null) {
                sentencia.setInt(4, objServicio.getDuracionMinutos());
            } else {
                sentencia.setNull(4, java.sql.Types.INTEGER);
            }

            if (objServicio.getFechaCreacion() != null) {
                sentencia.setDate(5, new java.sql.Date(objServicio.getFechaCreacion().getTime()));
            } else {
                sentencia.setNull(5, java.sql.Types.DATE);
            }

            sentencia.setInt(6, objServicio.getObjCategoria().getId());
            sentencia.setString(7, objServicio.getImagenBase64());
            sentencia.setString(8, objServicio.getEstado());
            sentencia.setInt(9, idServicio);

            resultado = sentencia.executeUpdate();

            sentencia.close();
            conexionABaseDeDatos.desconectar();

        } catch (SQLException e) {
            System.out.println("Error en la actualización: " + e.getMessage());
        }

        if (resultado == 1) {
            objServicioActualizado = this.findById(idServicio).orElse(null);
        }

        return objServicioActualizado == null ? Optional.empty() : Optional.of(objServicioActualizado);
    }

    /**
     * Eliminar servicio
     */
    public boolean delete(Integer idServicio) {
        System.out.println("Eliminando servicio de base de datos");
        int resultado = -1;
        conexionABaseDeDatos.conectar();
        try {
            String consulta = "DELETE FROM servicios WHERE id=?";
            PreparedStatement sentencia = conexionABaseDeDatos.getConnection().prepareStatement(consulta);
            sentencia.setInt(1, idServicio);
            resultado = sentencia.executeUpdate();
            sentencia.close();
            conexionABaseDeDatos.desconectar();
        } catch (SQLException e) {
            System.out.println("Error en la eliminación: " + e.getMessage());
        }

        return resultado == 1;
    }

    /**
     * Buscar por categoría
     */
    public Optional<Collection<ServicioEntity>> findByCategoria(Integer idCategoria) {
        System.out.println("Listando servicios por categoría: " + idCategoria);
        Collection<ServicioEntity> servicios = new LinkedList<>();

        conexionABaseDeDatos.conectar();
        try {
            String consulta = "SELECT servicios.*, categorias.nombreCategoria "
                    + "FROM servicios JOIN categorias ON servicios.idCategoria = categorias.id "
                    + "WHERE servicios.idCategoria = ?";
            PreparedStatement sentencia = conexionABaseDeDatos.getConnection().prepareStatement(consulta);
            sentencia.setInt(1, idCategoria);
            ResultSet res = sentencia.executeQuery();

            while (res.next()) {
                ServicioEntity objServicio = new ServicioEntity();
                objServicio.setId(res.getInt("id"));
                objServicio.setNombre(res.getString("nombre"));
                objServicio.setDescripcion(res.getString("descripcion"));

                double precioTemp = res.getDouble("precio");
                objServicio.setPrecio(res.wasNull() ? null : precioTemp);

                int duracionTemp = res.getInt("duracionMinutos");
                objServicio.setDuracionMinutos(res.wasNull() ? null : duracionTemp);

                objServicio.setFechaCreacion(res.getDate("fechaCreacion"));
                objServicio.setImagenBase64(res.getString("imagenBase64"));
                objServicio.setEstado(res.getString("estado"));

                objServicio.setObjCategoria(
                        new CategoriaEntity(res.getInt("idCategoria"), res.getString("nombreCategoria"))
                );

                servicios.add(objServicio);
            }

            sentencia.close();
            conexionABaseDeDatos.desconectar();
        } catch (SQLException e) {
            System.out.println("Error en la consulta por categoría: " + e.getMessage());
        }

        return servicios.isEmpty() ? Optional.empty() : Optional.of(servicios);
    }

    /**
     * Lista todos los servicios para el cliente
     */
    public Optional<Collection<ServicioEntity>> findAllClient() {
        System.out.println("Listando servicios de base de datos");
        Collection<ServicioEntity> servicios = new LinkedList<>();

        conexionABaseDeDatos.conectar();
        try {
            String consulta = "SELECT servicios.*, categorias.nombreCategoria "
                    + "FROM servicios JOIN categorias ON servicios.idCategoria = categorias.id "
                    + "WHERE servicios.estado = 'Activo'";
            PreparedStatement sentencia = conexionABaseDeDatos.getConnection().prepareStatement(consulta);
            ResultSet res = sentencia.executeQuery();

            while (res.next()) {
                ServicioEntity objServicio = new ServicioEntity();
                objServicio.setId(res.getInt("id"));
                objServicio.setNombre(res.getString("nombre"));
                objServicio.setDescripcion(res.getString("descripcion"));

                double precioTemp = res.getDouble("precio");
                objServicio.setPrecio(res.wasNull() ? null : precioTemp);

                int duracionTemp = res.getInt("duracionMinutos");
                objServicio.setDuracionMinutos(res.wasNull() ? null : duracionTemp);

                objServicio.setFechaCreacion(res.getDate("fechaCreacion"));
                objServicio.setImagenBase64(res.getString("imagenBase64"));
                objServicio.setEstado(res.getString("estado"));

                objServicio.setObjCategoria(
                        new CategoriaEntity(res.getInt("idCategoria"), res.getString("nombreCategoria"))
                );

                servicios.add(objServicio);
            }

            sentencia.close();
            conexionABaseDeDatos.desconectar();

        } catch (SQLException e) {
            System.out.println("Error en la consulta: " + e.getMessage());
        }

        return servicios.isEmpty() ? Optional.empty() : Optional.of(servicios);
    }

    /**
     * Buscar por categoría para cliente
     */
    public Optional<Collection<ServicioEntity>> findByCategoriaClient(Integer idCategoria) {
        System.out.println("Listando servicios por categoría: " + idCategoria);
        Collection<ServicioEntity> servicios = new LinkedList<>();

        conexionABaseDeDatos.conectar();
        try {
            String consulta = "SELECT servicios.*, categorias.nombreCategoria "
                    + "FROM servicios JOIN categorias ON servicios.idCategoria = categorias.id "
                    + "WHERE servicios.idCategoria = ? AND servicios.estado = 'Activo'";
            PreparedStatement sentencia = conexionABaseDeDatos.getConnection().prepareStatement(consulta);
            sentencia.setInt(1, idCategoria);
            ResultSet res = sentencia.executeQuery();

            while (res.next()) {
                ServicioEntity objServicio = new ServicioEntity();
                objServicio.setId(res.getInt("id"));
                objServicio.setNombre(res.getString("nombre"));
                objServicio.setDescripcion(res.getString("descripcion"));

                double precioTemp = res.getDouble("precio");
                objServicio.setPrecio(res.wasNull() ? null : precioTemp);

                int duracionTemp = res.getInt("duracionMinutos");
                objServicio.setDuracionMinutos(res.wasNull() ? null : duracionTemp);

                objServicio.setFechaCreacion(res.getDate("fechaCreacion"));
                objServicio.setImagenBase64(res.getString("imagenBase64"));
                objServicio.setEstado(res.getString("estado"));

                objServicio.setObjCategoria(
                        new CategoriaEntity(res.getInt("idCategoria"), res.getString("nombreCategoria"))
                );

                servicios.add(objServicio);
            }

            sentencia.close();
            conexionABaseDeDatos.desconectar();
        } catch (SQLException e) {
            System.out.println("Error en la consulta por categoría: " + e.getMessage());
        }

        return servicios.isEmpty() ? Optional.empty() : Optional.of(servicios);
    }
}
