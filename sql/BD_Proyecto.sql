DROP DATABASE IF EXISTS sistemaGestionEnvios;

CREATE DATABASE sistemaGestionEnvios
DEFAULT CHARACTER SET utf8mb4
DEFAULT COLLATE utf8mb4_unicode_ci;

USE sistemaGestionEnvios;

CREATE TABLE rol (
    id_rol INT NOT NULL AUTO_INCREMENT,
    nombre_rol VARCHAR(25) NOT NULL,
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    fecha_modificacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP
        ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (id_rol),
    UNIQUE (nombre_rol)
) ENGINE = InnoDB;

CREATE TABLE usuario (
    id_usuario INT NOT NULL AUTO_INCREMENT,
    id_rol INT NOT NULL,
    nombre VARCHAR(100) NOT NULL,
    correo VARCHAR(100) NOT NULL,
    contrasena VARCHAR(512) NOT NULL,
    activo BOOLEAN NOT NULL DEFAULT TRUE,
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    fecha_modificacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP
        ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (id_usuario),
    UNIQUE (correo),
    INDEX ndx_usuario_correo (correo),
    INDEX ndx_usuario_rol (id_rol),
    CHECK (
        correo REGEXP
        '^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$'
    ),
    CONSTRAINT fk_usuario_rol
        FOREIGN KEY (id_rol)
        REFERENCES rol (id_rol)
) ENGINE = InnoDB;

CREATE TABLE cliente (
    id_cliente INT NOT NULL AUTO_INCREMENT,
    id_usuario INT NOT NULL,
    telefono VARCHAR(25) NOT NULL,
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    fecha_modificacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP
        ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (id_cliente),
    UNIQUE (id_usuario),
    CONSTRAINT fk_cliente_usuario
        FOREIGN KEY (id_usuario)
        REFERENCES usuario (id_usuario)
) ENGINE = InnoDB;

CREATE TABLE repartidor (
    id_repartidor INT NOT NULL AUTO_INCREMENT,
    id_usuario INT NOT NULL,
    vehiculo VARCHAR(100) NOT NULL,
    disponible BOOLEAN NOT NULL DEFAULT TRUE,
    activo BOOLEAN NOT NULL DEFAULT TRUE,
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    fecha_modificacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP
        ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (id_repartidor),
    UNIQUE (id_usuario),
    INDEX ndx_repartidor_disponible (disponible),
    CONSTRAINT fk_repartidor_usuario
        FOREIGN KEY (id_usuario)
        REFERENCES usuario (id_usuario)
) ENGINE = InnoDB;

CREATE TABLE direccion (
    id_direccion INT NOT NULL AUTO_INCREMENT,
    id_cliente INT NULL,
    alias_direccion VARCHAR(50),
    provincia VARCHAR(50) NOT NULL,
    canton VARCHAR(50) NOT NULL,
    distrito VARCHAR(50) NOT NULL,
    direccion_exacta VARCHAR(500) NOT NULL,
    activo BOOLEAN NOT NULL DEFAULT TRUE,
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    fecha_modificacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP
        ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (id_direccion),
    INDEX ndx_direccion_cliente (id_cliente),
    INDEX ndx_direccion_ubicacion (
        provincia,
        canton,
        distrito
    ),
    CONSTRAINT fk_direccion_cliente
        FOREIGN KEY (id_cliente)
        REFERENCES cliente (id_cliente)
) ENGINE = InnoDB;

CREATE TABLE paquete (
    id_paquete INT NOT NULL AUTO_INCREMENT,
    descripcion VARCHAR(500) NOT NULL,
    peso DECIMAL(10,2) NOT NULL,
    dimensiones VARCHAR(100) NOT NULL,
    ruta_imagen VARCHAR(1024),
    activo BOOLEAN NOT NULL DEFAULT TRUE,
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    fecha_modificacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP
        ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (id_paquete),
    CHECK (peso > 0)
) ENGINE = InnoDB;

CREATE TABLE estado_envio (
    id_estado INT NOT NULL AUTO_INCREMENT,
    nombre_estado VARCHAR(40) NOT NULL,
    descripcion VARCHAR(250),
    activo BOOLEAN NOT NULL DEFAULT TRUE,
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    fecha_modificacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP
        ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (id_estado),
    UNIQUE (nombre_estado)
) ENGINE = InnoDB;

CREATE TABLE ruta_reparto (
    id_ruta INT NOT NULL AUTO_INCREMENT,
    id_repartidor INT NULL,
    nombre_ruta VARCHAR(100) NOT NULL,
    zona VARCHAR(100) NOT NULL,
    fecha_ruta DATE,
    activo BOOLEAN NOT NULL DEFAULT TRUE,
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    fecha_modificacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP
        ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (id_ruta),
    UNIQUE (nombre_ruta),
    INDEX ndx_ruta_repartidor (id_repartidor),
    INDEX ndx_ruta_zona (zona),
    CONSTRAINT fk_ruta_repartidor
        FOREIGN KEY (id_repartidor)
        REFERENCES repartidor (id_repartidor)
) ENGINE = InnoDB;

CREATE TABLE envio (
    id_envio INT NOT NULL AUTO_INCREMENT,
    codigo_seguimiento VARCHAR(30) NOT NULL,
    id_cliente INT NOT NULL,
    id_paquete INT NOT NULL,
    id_repartidor INT NULL,
    id_ruta INT NULL,
    id_direccion_origen INT NOT NULL,
    id_direccion_destino INT NOT NULL,
    id_estado INT NOT NULL,
    nombre_destinatario VARCHAR(100) NOT NULL,
    telefono_destinatario VARCHAR(25) NOT NULL,
    fecha_solicitud TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    fecha_recoleccion_estimada DATETIME NOT NULL,
    fecha_envio DATETIME NULL,
    fecha_entrega DATETIME NULL,
    motivo_rechazo VARCHAR(500) NULL,
    fecha_cancelacion DATETIME NULL,
    activo BOOLEAN NOT NULL DEFAULT TRUE,
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    fecha_modificacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP
        ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (id_envio),
    UNIQUE (codigo_seguimiento),
    UNIQUE (id_paquete),
    INDEX ndx_envio_cliente (id_cliente),
    INDEX ndx_envio_repartidor (id_repartidor),
    INDEX ndx_envio_ruta (id_ruta),
    INDEX ndx_envio_estado (id_estado),
    INDEX ndx_envio_codigo (codigo_seguimiento),
    INDEX ndx_envio_fecha (fecha_solicitud),
    CHECK (
        id_direccion_origen <> id_direccion_destino
    ),
    CONSTRAINT fk_envio_cliente
        FOREIGN KEY (id_cliente)
        REFERENCES cliente (id_cliente),
    CONSTRAINT fk_envio_paquete
        FOREIGN KEY (id_paquete)
        REFERENCES paquete (id_paquete),
    CONSTRAINT fk_envio_repartidor
        FOREIGN KEY (id_repartidor)
        REFERENCES repartidor (id_repartidor),
    CONSTRAINT fk_envio_ruta
        FOREIGN KEY (id_ruta)
        REFERENCES ruta_reparto (id_ruta),
    CONSTRAINT fk_envio_direccion_origen
        FOREIGN KEY (id_direccion_origen)
        REFERENCES direccion (id_direccion),
    CONSTRAINT fk_envio_direccion_destino
        FOREIGN KEY (id_direccion_destino)
        REFERENCES direccion (id_direccion),
    CONSTRAINT fk_envio_estado
        FOREIGN KEY (id_estado)
        REFERENCES estado_envio (id_estado)
) ENGINE = InnoDB;

CREATE TABLE historial_envio (
    id_historial INT NOT NULL AUTO_INCREMENT,
    id_envio INT NOT NULL,
    id_estado INT NOT NULL,
    id_usuario INT NOT NULL,
    fecha_cambio TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    observacion VARCHAR(500),
    ruta_evidencia VARCHAR(1024),
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    fecha_modificacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP
        ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (id_historial),
    INDEX ndx_historial_envio (id_envio),
    INDEX ndx_historial_estado (id_estado),
    INDEX ndx_historial_usuario (id_usuario),
    INDEX ndx_historial_fecha (fecha_cambio),
    CONSTRAINT fk_historial_envio
        FOREIGN KEY (id_envio)
        REFERENCES envio (id_envio),
    CONSTRAINT fk_historial_estado
        FOREIGN KEY (id_estado)
        REFERENCES estado_envio (id_estado),
    CONSTRAINT fk_historial_usuario
        FOREIGN KEY (id_usuario)
        REFERENCES usuario (id_usuario)
) ENGINE = InnoDB;

INSERT INTO rol (nombre_rol) VALUES
('ADMINISTRADOR'),
('CLIENTE'),
('REPARTIDOR');

INSERT INTO estado_envio (nombre_estado, descripcion) VALUES
('REGISTRADO', 'El envío fue registrado en el sistema.'),
('PENDIENTE_RECOLECCION', 'La solicitud está pendiente de revisión.'),
('APROBADO', 'La solicitud fue aprobada por el administrador.'),
('ASIGNADO', 'El envío fue asignado a un repartidor.'),
('RECOGIDO', 'El paquete fue recogido en la dirección de origen.'),
('EN_TRANSITO', 'El paquete se encuentra en camino.'),
('ENTREGADO', 'El paquete fue entregado al destinatario.'),
('INTENTO_FALLIDO', 'No fue posible realizar la entrega.'),
('RECHAZADO', 'La solicitud fue rechazada.'),
('CANCELADO', 'La solicitud fue cancelada por el cliente.');

INSERT INTO usuario (id_rol, nombre, correo, contrasena, activo) VALUES
(1, 'Administrador Sistema', 'admin@envios.cr', 'Admin123*', TRUE),
(2, 'María Rodríguez', 'cliente@envios.cr', 'Cliente123*', TRUE),
(3, 'Carlos Vargas', 'repartidor1@envios.cr', 'Repartidor123*', TRUE),
(3, 'Andrea Jiménez', 'repartidor2@envios.cr', 'Repartidor123*', TRUE);

INSERT INTO cliente (id_usuario, telefono) VALUES
(2, '8888-0002');

INSERT INTO repartidor (id_usuario, vehiculo, disponible, activo) VALUES
(3, 'Motocicleta placa M-123456', TRUE, TRUE),
(4, 'Automóvil placa ABC-789', TRUE, TRUE);

INSERT INTO direccion (id_cliente, alias_direccion, provincia, canton, distrito, direccion_exacta, activo) VALUES
(1, 'Casa', 'San José', 'Desamparados', 'San Rafael Arriba', '200 metros al norte de la iglesia católica.', TRUE),
(1, 'Trabajo', 'San José', 'San José', 'Carmen', 'Edificio Central, segundo piso.', TRUE),
(1, 'Destino Heredia', 'Heredia', 'Heredia', 'Mercedes', 'Frente al parque de Mercedes.', TRUE),
(1, 'Destino Cartago', 'Cartago', 'Cartago', 'Oriental', '100 metros al este del mercado municipal.', TRUE);

INSERT INTO paquete (descripcion, peso, dimensiones, ruta_imagen, activo) VALUES
('Caja con documentos universitarios', 1.25, '30x20x10 cm', NULL, TRUE),
('Paquete con accesorios electrónicos', 2.80, '40x30x20 cm', NULL, TRUE),
('Caja con artículos personales', 4.50, '50x40x30 cm', NULL, TRUE);

INSERT INTO ruta_reparto (id_repartidor, nombre_ruta, zona, fecha_ruta, activo) VALUES
(1, 'Ruta San José', 'San José', CURRENT_DATE, TRUE),
(2, 'Ruta Heredia', 'Heredia', CURRENT_DATE, TRUE);

INSERT INTO envio (codigo_seguimiento, id_cliente, id_paquete, id_repartidor, id_ruta, id_direccion_origen, id_direccion_destino, id_estado, nombre_destinatario, telefono_destinatario, fecha_recoleccion_estimada, fecha_envio, fecha_entrega, motivo_rechazo, fecha_cancelacion, activo) VALUES
('ENV-2026-000001', 1, 1, NULL, NULL, 1, 3, 2, 'José Hernández', '8777-1111', DATE_ADD(CURRENT_TIMESTAMP, INTERVAL 1 DAY), NULL, NULL, NULL, NULL, TRUE),
('ENV-2026-000002', 1, 2, 1, 1, 2, 4, 4, 'Laura Ramírez', '8777-2222', DATE_ADD(CURRENT_TIMESTAMP, INTERVAL 2 DAY), NULL, NULL, NULL, NULL, TRUE),
('ENV-2026-000003', 1, 3, 2, 2, 1, 3, 7, 'Daniel Chaves', '8777-3333', DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 4 DAY), DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 3 DAY), DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 2 DAY), NULL, NULL, TRUE);

INSERT INTO historial_envio (id_envio, id_estado, id_usuario, fecha_cambio, observacion, ruta_evidencia) VALUES
(1, 2, 2, CURRENT_TIMESTAMP, 'Solicitud registrada por el cliente.', NULL),
(2, 2, 2, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 2 HOUR), 'Solicitud registrada por el cliente.', NULL),
(2, 3, 1, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 90 MINUTE), 'Solicitud aprobada por el administrador.', NULL),
(2, 4, 1, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 60 MINUTE), 'Envío asignado al repartidor Carlos Vargas.', NULL),
(3, 2, 2, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 5 DAY), 'Solicitud registrada por el cliente.', NULL),
(3, 4, 1, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 4 DAY), 'Envío asignado a la repartidora Andrea Jiménez.', NULL),
(3, 5, 4, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 3 DAY), 'Paquete recogido correctamente.', NULL),
(3, 6, 4, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 60 HOUR), 'Paquete en tránsito hacia el destino.', NULL),
(3, 7, 4, DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 2 DAY), 'Paquete entregado al destinatario.', NULL);