DROP DATABASE IF EXISTS sistemaGestionEnvios;
DROP USER IF EXISTS 'usuario_app'@'%';
DROP USER IF EXISTS 'usuario_reportes'@'%';

CREATE DATABASE sistemaGestionEnvios
  DEFAULT CHARACTER SET utf8mb4
  DEFAULT COLLATE utf8mb4_unicode_ci;

CREATE USER 'usuario_app'@'%' IDENTIFIED BY 'App_Clave123.';
CREATE USER 'usuario_reportes'@'%' IDENTIFIED BY 'Reportes_Clave123.';

GRANT SELECT, INSERT, UPDATE, DELETE ON sistema_envios.* TO 'usuario_app'@'%';
GRANT SELECT ON sistema_envios.* TO 'usuario_reportes'@'%';

FLUSH PRIVILEGES;

USE sistemaGestionEnvios;

CREATE TABLE roles (
  id_rol INT NOT NULL AUTO_INCREMENT,
  nombre_rol VARCHAR(30) NOT NULL UNIQUE,
  fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  fecha_modificacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (id_rol)
) ENGINE = InnoDB;

CREATE TABLE usuarios (
  id_usuario INT NOT NULL AUTO_INCREMENT,
  nombre VARCHAR(50) NOT NULL,
  apellidos VARCHAR(80) NOT NULL,
  correo VARCHAR(100) NOT NULL UNIQUE,
  password VARCHAR(512) NOT NULL,
  telefono VARCHAR(25),
  id_rol INT NOT NULL,
  estado VARCHAR(20) DEFAULT 'Activo',
  fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  fecha_modificacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (id_usuario),
  FOREIGN KEY fk_usuarios_roles (id_rol) REFERENCES roles(id_rol),
  CHECK (correo REGEXP '^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$'),
  INDEX ndx_usuarios_correo (correo),
  INDEX ndx_usuarios_rol (id_rol)
) ENGINE = InnoDB;

CREATE TABLE clientes (
  id_cliente INT NOT NULL AUTO_INCREMENT,
  id_usuario INT NOT NULL,
  telefono VARCHAR(25),
  fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  fecha_modificacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (id_cliente),
  UNIQUE (id_usuario),
  FOREIGN KEY fk_clientes_usuarios (id_usuario) REFERENCES usuarios(id_usuario)
) ENGINE = InnoDB;

CREATE TABLE repartidores (
  id_repartidor INT NOT NULL AUTO_INCREMENT,
  id_usuario INT NOT NULL,
  vehiculo VARCHAR(100),
  estado VARCHAR(30) DEFAULT 'Disponible',
  fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  fecha_modificacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (id_repartidor),
  UNIQUE (id_usuario),
  FOREIGN KEY fk_repartidores_usuarios (id_usuario) REFERENCES usuarios(id_usuario)
) ENGINE = InnoDB;

CREATE TABLE direcciones (
  id_direccion INT NOT NULL AUTO_INCREMENT,
  provincia VARCHAR(50) NOT NULL,
  canton VARCHAR(50) NOT NULL,
  distrito VARCHAR(50) NOT NULL,
  direccion_exacta VARCHAR(255) NOT NULL,
  fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  fecha_modificacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (id_direccion),
  INDEX ndx_direcciones_provincia (provincia),
  INDEX ndx_direcciones_canton (canton)
) ENGINE = InnoDB;

CREATE TABLE paquetes (
  id_paquete INT NOT NULL AUTO_INCREMENT,
  descripcion VARCHAR(150) NOT NULL,
  peso DECIMAL(10,2) NOT NULL CHECK (peso >= 0),
  dimensiones VARCHAR(100),
  fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  fecha_modificacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (id_paquete),
  INDEX ndx_paquetes_descripcion (descripcion)
) ENGINE = InnoDB;

CREATE TABLE estados_envio (
  id_estado INT NOT NULL AUTO_INCREMENT,
  nombre_estado VARCHAR(50) NOT NULL UNIQUE,
  fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  fecha_modificacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (id_estado)
) ENGINE = InnoDB;

CREATE TABLE rutas (
  id_ruta INT NOT NULL AUTO_INCREMENT,
  nombre_ruta VARCHAR(100) NOT NULL,
  zona VARCHAR(100) NOT NULL,
  fecha_ruta DATE NOT NULL,
  id_repartidor INT NOT NULL,
  fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  fecha_modificacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (id_ruta),
  FOREIGN KEY fk_rutas_repartidores (id_repartidor) REFERENCES repartidores(id_repartidor),
  INDEX ndx_rutas_repartidor (id_repartidor)
) ENGINE = InnoDB;

CREATE TABLE envios (
  id_envio INT NOT NULL AUTO_INCREMENT,
  id_cliente INT NOT NULL,
  id_paquete INT NOT NULL,
  id_repartidor INT NULL,
  id_ruta INT NULL,
  id_direccion_origen INT NOT NULL,
  id_direccion_destino INT NOT NULL,
  id_estado INT NOT NULL,
  codigo_seguimiento VARCHAR(30) NOT NULL UNIQUE,
  nombre_destinatario VARCHAR(100),
  telefono_destinatario VARCHAR(25),
  fecha_recoleccion_estimada DATETIME NULL,
  fecha_envio TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  observacion VARCHAR(255),
  estado VARCHAR(20) DEFAULT 'Activo',
  fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  fecha_modificacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (id_envio),
  FOREIGN KEY fk_envios_clientes (id_cliente) REFERENCES clientes(id_cliente),
  FOREIGN KEY fk_envios_paquetes (id_paquete) REFERENCES paquetes(id_paquete),
  FOREIGN KEY fk_envios_repartidores (id_repartidor) REFERENCES repartidores(id_repartidor),
  FOREIGN KEY fk_envios_rutas (id_ruta) REFERENCES rutas(id_ruta),
  FOREIGN KEY fk_envios_direccion_origen (id_direccion_origen) REFERENCES direcciones(id_direccion),
  FOREIGN KEY fk_envios_direccion_destino (id_direccion_destino) REFERENCES direcciones(id_direccion),
  FOREIGN KEY fk_envios_estados (id_estado) REFERENCES estados_envio(id_estado),
  INDEX ndx_envios_cliente (id_cliente),
  INDEX ndx_envios_repartidor (id_repartidor),
  INDEX ndx_envios_ruta (id_ruta),
  INDEX ndx_envios_estado (id_estado),
  INDEX ndx_envios_codigo (codigo_seguimiento)
) ENGINE = InnoDB;

CREATE TABLE historial_envios (
  id_historial INT NOT NULL AUTO_INCREMENT,
  id_envio INT NOT NULL,
  id_estado INT NOT NULL,
  fecha_cambio TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  observacion VARCHAR(255),
  fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  fecha_modificacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (id_historial),
  FOREIGN KEY fk_historial_envios_envios (id_envio) REFERENCES envios(id_envio),
  FOREIGN KEY fk_historial_envios_estados (id_estado) REFERENCES estados_envio(id_estado),
  INDEX ndx_historial_envio (id_envio),
  INDEX ndx_historial_estado (id_estado)
) ENGINE = InnoDB;

INSERT INTO roles (nombre_rol) VALUES
('ADMIN'),
('CLIENTE'),
('REPARTIDOR');

INSERT INTO usuarios (nombre, apellidos, correo, password, telefono, id_rol, estado) VALUES
('Administrador', 'Sistema', 'admin@sistemaenvios.com', '$2y$10$qzkW4/6HGDOZLw8bvUAUuehReykCiNjU7XwfKH9sO5JJZNMFXo0GK', '8888-0001', 1, 'Activo'),
('María', 'Gómez Soto', 'cliente1@gmail.com', '$2y$10$xn0gFS4B.T3//XHHPbDBXu4UfvZ1QmWa3WDBHi1WXQhiYnYrCvWMK', '8888-0002', 2, 'Activo'),
('Carlos', 'Mora Rojas', 'cliente2@gmail.com', '$2y$10$xn0gFS4B.T3//XHHPbDBXu4UfvZ1QmWa3WDBHi1WXQhiYnYrCvWMK', '8888-0003', 2, 'Activo'),
('Luis', 'Fernández Castro', 'repartidor1@gmail.com', '$2y$10$fQQLV9i9JK4FdhaKCA/hxuLDrMozQROxKZGMDq.r/OphJ2BBihx4m', '8888-0004', 3, 'Activo'),
('Ana', 'Ramírez López', 'repartidor2@gmail.com', '$2y$10$fQQLV9i9JK4FdhaKCA/hxuLDrMozQROxKZGMDq.r/OphJ2BBihx4m', '8888-0005', 3, 'Activo');

INSERT INTO clientes (id_usuario, telefono) VALUES
(2, '8888-0002'),
(3, '8888-0003');

INSERT INTO repartidores (id_usuario, vehiculo, estado) VALUES
(4, 'Motocicleta Honda Navi', 'Disponible'),
(5, 'Motocicleta Yamaha Ray ZR', 'Disponible');

INSERT INTO direcciones (provincia, canton, distrito, direccion_exacta) VALUES
('San José', 'Desamparados', 'San Rafael Arriba', 'Del parque central 200 metros al este, casa color blanco'),
('San José', 'Curridabat', 'Granadilla', 'Condominio Los Pinos, casa 15'),
('San José', 'Montes de Oca', 'San Pedro', 'Frente a la Universidad Fidélitas'),
('Alajuela', 'Alajuela', 'San Rafael', 'Mall Plaza Real, local 22'),
('Heredia', 'Heredia', 'Ulloa', 'Zona franca, edificio 4'),
('Cartago', 'La Unión', 'Tres Ríos', 'Centro comercial, segundo piso');

INSERT INTO paquetes (descripcion, peso, dimensiones) VALUES
('Caja pequeña con documentos', 1.20, '30x20x10 cm'),
('Paquete mediano con ropa', 3.50, '45x35x20 cm'),
('Caja con accesorios electrónicos', 2.80, '40x30x25 cm'),
('Paquete frágil', 4.00, '50x40x30 cm');

INSERT INTO estados_envio (nombre_estado) VALUES
('Registrado'),
('Pendiente de recolección'),
('Asignado'),
('En ruta de recolección'),
('Paquete recogido'),
('En tránsito'),
('Entregado'),
('Intento fallido'),
('Cancelado'),
('Rechazado');

INSERT INTO rutas (nombre_ruta, zona, fecha_ruta, id_repartidor) VALUES
('Ruta San José Este', 'San Pedro - Curridabat - Tres Ríos', '2026-07-04', 1),
('Ruta GAM Norte', 'Heredia - Alajuela', '2026-07-04', 2);

INSERT INTO envios (
  id_cliente,
  id_paquete,
  id_repartidor,
  id_ruta,
  id_direccion_origen,
  id_direccion_destino,
  id_estado,
  codigo_seguimiento,
  nombre_destinatario,
  telefono_destinatario,
  fecha_recoleccion_estimada,
  fecha_envio,
  observacion,
  estado
) VALUES
(1, 1, 1, 1, 1, 2, 6, 'ENV-2026-0001', 'Sofía Vargas', '7000-0001', '2026-07-04 08:30:00', '2026-07-04 08:30:00', 'Paquete en tránsito hacia Curridabat', 'Activo'),
(1, 2, NULL, NULL, 1, 3, 2, 'ENV-2026-0002', 'Andrés Castillo', '7000-0002', '2026-07-04 09:00:00', '2026-07-04 09:00:00', 'Solicitud pendiente de asignación', 'Activo'),
(2, 3, 2, 2, 4, 5, 3, 'ENV-2026-0003', 'Valeria Jiménez', '7000-0003', '2026-07-04 10:15:00', '2026-07-04 10:15:00', 'Asignado a repartidor', 'Activo'),
(2, 4, 1, 1, 6, 1, 7, 'ENV-2026-0004', 'Daniel Rojas', '7000-0004', '2026-07-03 14:20:00', '2026-07-03 14:20:00', 'Entregado correctamente', 'Activo');

INSERT INTO historial_envios (id_envio, id_estado, fecha_cambio, observacion) VALUES
(1, 1, '2026-07-04 08:30:00', 'Envío registrado por el cliente'),
(1, 3, '2026-07-04 08:45:00', 'Administrador asignó repartidor'),
(1, 6, '2026-07-04 09:15:00', 'Repartidor marcó el paquete en tránsito'),
(2, 1, '2026-07-04 09:00:00', 'Envío registrado por el cliente'),
(2, 2, '2026-07-04 09:05:00', 'Pendiente de recolección'),
(3, 1, '2026-07-04 10:15:00', 'Envío registrado'),
(3, 3, '2026-07-04 10:25:00', 'Asignado a repartidor'),
(4, 1, '2026-07-03 14:20:00', 'Envío registrado'),
(4, 6, '2026-07-03 15:00:00', 'Paquete en tránsito'),
(4, 7, '2026-07-03 16:10:00', 'Paquete entregado correctamente');