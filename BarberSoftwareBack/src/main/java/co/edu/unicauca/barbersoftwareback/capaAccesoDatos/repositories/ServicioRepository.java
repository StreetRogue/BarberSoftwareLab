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
     * @param objServicio entidad servicio con datos a guardar
     * @return Optional con el servicio almacenado (con id generado) o Optional.empty() si fallo
     */
    public Optional<ServicioEntity> save(ServicioEntity objServicio) {
        System.out.println("registrando servicio en base de datos");
        ServicioEntity objServicioAlmacenado = null;
        int resultado = -1;

        try {
            conexionABaseDeDatos.conectar();

            PreparedStatement sentencia = null;
            String consulta = "insert into servicios(nombre, descripcion, precio, duracionMinutos, fechaCreacion, idCategoria) values(?,?,?,?,?,?)";
            sentencia = conexionABaseDeDatos.getConnection()
                    .prepareStatement(consulta, Statement.RETURN_GENERATED_KEYS);

            sentencia.setString(1, objServicio.getNombre());
            sentencia.setString(2, objServicio.getDescripcion());
            // precio puede ser null, manejar si necesario
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
                sentencia.setDate(5, new java.sql.Date(new java.util.Date().getTime())); // o CURRENT_DATE
            }
            sentencia.setInt(6, objServicio.getObjCategoria().getId());

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
            System.out.println("error en la inserción: " + e.getMessage());
        }

        return objServicioAlmacenado == null ? Optional.empty() : Optional.of(objServicioAlmacenado);
    }

    /**
     * Lista todos los servicios (join con categorias para devolver la info de la categoria)
     */
    public Optional<Collection<ServicioEntity>> findAll() {
        System.out.println("listando servicios de base de datos");
        Collection<ServicioEntity> servicios = new LinkedList<ServicioEntity>();

        conexionABaseDeDatos.conectar();
        try {
            PreparedStatement sentencia = null;
            String consulta = "select servicios.* , categorias.nombreCategoria "
                    + "from servicios join categorias on servicios.idCategoria=categorias.id";
            sentencia = conexionABaseDeDatos.getConnection().prepareStatement(consulta);
            ResultSet res = sentencia.executeQuery();
            while (res.next()) {
                ServicioEntity objServicio = new ServicioEntity();
                objServicio.setId(res.getInt("id"));
                objServicio.setNombre(res.getString("nombre"));
                objServicio.setDescripcion(res.getString("descripcion"));
                // precio puede ser null en BD; obtener con getDouble + wasNull
                double precioTemp = res.getDouble("precio");
                if (res.wasNull()) {
                    objServicio.setPrecio(null);
                } else {
                    objServicio.setPrecio(precioTemp);
                }
                int duracionTemp = res.getInt("duracionMinutos");
                if (res.wasNull()) {
                    objServicio.setDuracionMinutos(null);
                } else {
                    objServicio.setDuracionMinutos(duracionTemp);
                }
                objServicio.setFechaCreacion(res.getDate("fechaCreacion"));
                objServicio.setObjCategoria(new CategoriaEntity(res.getInt("idCategoria"), res.getString("nombreCategoria")));
                servicios.add(objServicio);
            }
            sentencia.close();
            conexionABaseDeDatos.desconectar();

        } catch (SQLException e) {
            System.out.println("error en la consulta: " + e.getMessage());
        }

        return servicios.isEmpty() ? Optional.empty() : Optional.of(servicios);
    }

    /**
     * Buscar por id
     */
    public Optional<ServicioEntity> findById(Integer idServicio) {
        System.out.println("consultar servicio de base de datos");
        ServicioEntity objServicio = null;

        conexionABaseDeDatos.conectar();
        try {
            PreparedStatement sentencia = null;
            String consulta = "select servicios.* , categorias.nombreCategoria "
                    + "from servicios join categorias on servicios.idCategoria=categorias.id "
                    + "where servicios.id=?";
            sentencia = conexionABaseDeDatos.getConnection().prepareStatement(consulta);
            sentencia.setInt(1, idServicio);
            ResultSet res = sentencia.executeQuery();
            while (res.next()) {
                System.out.println("servicio encontrado");
                objServicio = new ServicioEntity();
                objServicio.setId(res.getInt("id"));
                objServicio.setNombre(res.getString("nombre"));
                objServicio.setDescripcion(res.getString("descripcion"));
                double precioTemp = res.getDouble("precio");
                if (res.wasNull()) {
                    objServicio.setPrecio(null);
                } else {
                    objServicio.setPrecio(precioTemp);
                }
                int duracionTemp = res.getInt("duracionMinutos");
                if (res.wasNull()) {
                    objServicio.setDuracionMinutos(null);
                } else {
                    objServicio.setDuracionMinutos(duracionTemp);
                }
                objServicio.setFechaCreacion(res.getDate("fechaCreacion"));
                objServicio.setObjCategoria(new CategoriaEntity(res.getInt("idCategoria"), res.getString("nombreCategoria")));
            }
            sentencia.close();
            conexionABaseDeDatos.desconectar();

        } catch (SQLException e) {
            System.out.println("error en la consulta: " + e.getMessage());
        }

        return objServicio == null ? Optional.empty() : Optional.of(objServicio);
    }

    /**
     * Actualizar servicio
     */
    public Optional<ServicioEntity> update(Integer idServicio, ServicioEntity objServicio) {
        System.out.println("actualizar servicio de base de datos");
        ServicioEntity objServicioActualizado = null;
        conexionABaseDeDatos.conectar();
        int resultado = -1;
        try {
            PreparedStatement sentencia = null;
            String consulta = "update servicios set nombre=?, descripcion=?, precio=?, duracionMinutos=?, fechaCreacion=?, idCategoria=? where id=?";
            sentencia = conexionABaseDeDatos.getConnection().prepareStatement(consulta);

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
            sentencia.setInt(7, idServicio);

            resultado = sentencia.executeUpdate();
            sentencia.close();
            conexionABaseDeDatos.desconectar();

        } catch (SQLException e) {
            System.out.println("error en la actualización: " + e.getMessage());
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
        System.out.println("eliminar servicio de base de datos");
        conexionABaseDeDatos.conectar();
        int resultado = -1;
        try {
            PreparedStatement sentencia = null;
            String consulta = "delete from servicios where id=?";
            sentencia = conexionABaseDeDatos.getConnection().prepareStatement(consulta);
            sentencia.setInt(1, idServicio);
            resultado = sentencia.executeUpdate();
            sentencia.close();
            conexionABaseDeDatos.desconectar();

        } catch (SQLException e) {
            System.out.println("error en la eliminación: " + e.getMessage());
        }

        return resultado == 1;
    }

    /**
     * Lista servicios por idCategoria
     */
    public Optional<Collection<ServicioEntity>> findByCategoria(Integer idCategoria) {
        System.out.println("listando servicios por categoria: " + idCategoria);
        Collection<ServicioEntity> servicios = new LinkedList<ServicioEntity>();

        conexionABaseDeDatos.conectar();
        try {
            PreparedStatement sentencia = null;
            String consulta = "select servicios.* , categorias.nombreCategoria "
                    + "from servicios join categorias on servicios.idCategoria=categorias.id "
                    + "where servicios.idCategoria = ?";
            sentencia = conexionABaseDeDatos.getConnection().prepareStatement(consulta);
            sentencia.setInt(1, idCategoria);
            ResultSet res = sentencia.executeQuery();
            while (res.next()) {
                ServicioEntity objServicio = new ServicioEntity();
                objServicio.setId(res.getInt("id"));
                objServicio.setNombre(res.getString("nombre"));
                objServicio.setDescripcion(res.getString("descripcion"));
                double precioTemp = res.getDouble("precio");
                if (res.wasNull()) {
                    objServicio.setPrecio(null);
                } else {
                    objServicio.setPrecio(precioTemp);
                }
                int duracionTemp = res.getInt("duracionMinutos");
                if (res.wasNull()) {
                    objServicio.setDuracionMinutos(null);
                } else {
                    objServicio.setDuracionMinutos(duracionTemp);
                }
                objServicio.setFechaCreacion(res.getDate("fechaCreacion"));
                objServicio.setObjCategoria(new CategoriaEntity(res.getInt("idCategoria"), res.getString("nombreCategoria")));
                servicios.add(objServicio);
            }
            sentencia.close();
            conexionABaseDeDatos.desconectar();

        } catch (SQLException e) {
            System.out.println("error en la consulta por categoria: " + e.getMessage());
        }

        return servicios.isEmpty() ? Optional.empty() : Optional.of(servicios);
    }

}
