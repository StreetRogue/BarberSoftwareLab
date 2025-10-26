CREATE TABLE categorias (
                            id INT AUTO_INCREMENT PRIMARY KEY,
                            nombreCategoria VARCHAR(255)
);

CREATE TABLE servicios (
                           id INT AUTO_INCREMENT PRIMARY KEY,
                           nombre VARCHAR(255) NOT NULL,
                           descripcion VARCHAR(500),
                           precio DECIMAL(10,2),
                           duracionMinutos INT,
                           fechaCreacion DATE,
                           idCategoria INT,
                           FOREIGN KEY (idCategoria) REFERENCES categorias(id)
);
