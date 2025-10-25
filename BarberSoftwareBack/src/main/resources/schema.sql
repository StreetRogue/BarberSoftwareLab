CREATE TABLE categorias (
                            id INT AUTO_INCREMENT PRIMARY KEY,
                            nombreCategoria VARCHAR(255)
);

CREATE TABLE clientes (
                          id INT AUTO_INCREMENT PRIMARY KEY,
                          nombre VARCHAR(255),
                          apellido VARCHAR(255),
                          email VARCHAR(255),
                          createAt DATE,
                          idCategoria INT,
                          FOREIGN KEY (idCategoria) REFERENCES categorias(id)
);