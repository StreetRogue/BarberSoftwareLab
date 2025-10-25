package co.edu.unicauca.barbersoftwareback.capaAccesoDatos.repositories.conexion;

import java.sql.*;

public class ConexionDB {
    private Connection connection;

    public ConexionDB() {}

    public void conectar() {
        try {
            Class.forName("org.h2.Driver");
            connection = DriverManager.
                    getConnection("jdbc:h2:mem:Barberdb", "sa", "");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Connection getConnection() {
        return connection;
    }

    public void desconectar() {
        try {
            if (connection != null) connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
