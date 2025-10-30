INSERT INTO categorias (id, nombreCategoria) VALUES (1, 'Marinero');
INSERT INTO categorias (id, nombreCategoria) VALUES (2, 'Corsario');
INSERT INTO categorias (id, nombreCategoria) VALUES (3, 'Barbenegra');
INSERT INTO categorias (id, nombreCategoria) VALUES (4, 'ELITE');

INSERT INTO servicios (nombre, descripcion, precio, duracionMinutos, fechaCreacion, idCategoria, estado)
VALUES ('Corte Clásico', 'Corte tradicional con tijera y máquina', 20000, 30, '2025-01-22', 1, 'Activo');

INSERT INTO servicios (nombre, descripcion, precio, duracionMinutos, fechaCreacion, idCategoria, estado)
VALUES ('Afeitado Completo', 'Afeitado con toalla caliente y loción', 15000, 25, '2025-03-22', 2, 'Activo');

INSERT INTO servicios (nombre, descripcion, precio, duracionMinutos, fechaCreacion, idCategoria, estado)
VALUES ('Tinte Capilar', 'Aplicación de tinte profesional para cabello', 30000, 45, '2025-06-22', 3, 'Inactivo');

INSERT INTO servicios (nombre, descripcion, precio, duracionMinutos, fechaCreacion, idCategoria, estado)
VALUES ('Corte y Barba', 'Combo de corte moderno y arreglo de barba', 35000, 60, '2025-04-22',3,'Activo');