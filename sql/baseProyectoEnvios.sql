DROP DATABASE IF EXISTS sistemaGestionEnvios;

CREATE DATABASE sistemaGestionEnvios
    DEFAULT CHARACTER SET utf8mb4
    DEFAULT COLLATE utf8mb4_unicode_ci;

USE sistemaGestionEnvios;

CREATE TABLE usuario (
    id_usuario INT NOT NULL AUTO_INCREMENT,
    username VARCHAR(30) NOT NULL UNIQUE,
    password VARCHAR(512),
    nombre VARCHAR(20) NOT NULL,
    apellidos VARCHAR(30) NOT NULL,
    correo VARCHAR(75) UNIQUE,
    telefono VARCHAR(25),
    ruta_imagen VARCHAR(1024),
    activo BOOLEAN NOT NULL DEFAULT TRUE,
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    fecha_modificacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP
        ON UPDATE CURRENT_TIMESTAMP,

    PRIMARY KEY (id_usuario),

    INDEX ndx_username (username)
)
ENGINE = InnoDB;

CREATE TABLE rol (
    id_rol INT NOT NULL AUTO_INCREMENT,
    rol VARCHAR(25) UNIQUE,
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    fecha_modificacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP
        ON UPDATE CURRENT_TIMESTAMP,

    PRIMARY KEY (id_rol)
)
ENGINE = InnoDB;

CREATE TABLE usuario_rol (
    id_usuario INT NOT NULL,
    id_rol INT NOT NULL,

    PRIMARY KEY (id_usuario, id_rol),

    CONSTRAINT fk_usuario_rol_usuario
        FOREIGN KEY (id_usuario)
        REFERENCES usuario(id_usuario),

    CONSTRAINT fk_usuario_rol_rol
        FOREIGN KEY (id_rol)
        REFERENCES rol(id_rol)
)
ENGINE = InnoDB;

CREATE TABLE ruta (
    id_ruta INT NOT NULL AUTO_INCREMENT,
    ruta VARCHAR(255) NOT NULL,
    requiere_rol BOOLEAN NOT NULL DEFAULT TRUE,
    id_rol INT NULL,
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    fecha_modificacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP
        ON UPDATE CURRENT_TIMESTAMP,

    PRIMARY KEY (id_ruta),

    CONSTRAINT fk_ruta_rol
        FOREIGN KEY (id_rol)
        REFERENCES rol(id_rol)
)
ENGINE = InnoDB;

CREATE TABLE constante (
    id_constante INT NOT NULL AUTO_INCREMENT,
    atributo VARCHAR(25) NOT NULL UNIQUE,
    valor VARCHAR(150) NOT NULL,
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    fecha_modificacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP
        ON UPDATE CURRENT_TIMESTAMP,

    PRIMARY KEY (id_constante)
)
ENGINE = InnoDB;

CREATE TABLE direccion (
    id_direccion INT NOT NULL AUTO_INCREMENT,
    provincia VARCHAR(50) NOT NULL,
    canton VARCHAR(50) NOT NULL,
    distrito VARCHAR(50) NOT NULL,
    direccion_exacta VARCHAR(255) NOT NULL,
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    fecha_modificacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP
        ON UPDATE CURRENT_TIMESTAMP,

    PRIMARY KEY (id_direccion),

    INDEX ndx_provincia (provincia),
    INDEX ndx_canton (canton)
)
ENGINE = InnoDB;

CREATE TABLE cliente (
    id_cliente INT NOT NULL AUTO_INCREMENT,
    id_usuario INT NOT NULL UNIQUE,
    telefono VARCHAR(25),
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    fecha_modificacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP
        ON UPDATE CURRENT_TIMESTAMP,

    PRIMARY KEY (id_cliente),

    CONSTRAINT fk_cliente_usuario
        FOREIGN KEY (id_usuario)
        REFERENCES usuario(id_usuario)
)
ENGINE = InnoDB;

CREATE TABLE repartidor (
    id_repartidor INT NOT NULL AUTO_INCREMENT,
    id_usuario INT NOT NULL UNIQUE,
    vehiculo VARCHAR(100),
    estado VARCHAR(30),
    foto_url VARCHAR(500),
    licencia_url VARCHAR(500),
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    fecha_modificacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP
        ON UPDATE CURRENT_TIMESTAMP,

    PRIMARY KEY (id_repartidor),

    CONSTRAINT fk_repartidor_usuario
        FOREIGN KEY (id_usuario)
        REFERENCES usuario(id_usuario),

    INDEX ndx_repartidor_estado (estado)
)
ENGINE = InnoDB;

CREATE TABLE paquete (
    id_paquete INT NOT NULL AUTO_INCREMENT,
    descripcion VARCHAR(150) NOT NULL,
    peso DECIMAL(10,2),
    dimensiones VARCHAR(100),
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    fecha_modificacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP
        ON UPDATE CURRENT_TIMESTAMP,

    PRIMARY KEY (id_paquete)
)
ENGINE = InnoDB;

CREATE TABLE estado_envio (
    id_estado INT NOT NULL AUTO_INCREMENT,
    nombre_estado VARCHAR(50) NOT NULL UNIQUE,
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    fecha_modificacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP
        ON UPDATE CURRENT_TIMESTAMP,

    PRIMARY KEY (id_estado)
)
ENGINE = InnoDB;

CREATE TABLE solicitud_recoleccion (
    id_solicitud INT NOT NULL AUTO_INCREMENT,

    id_cliente INT NOT NULL,
    id_direccion_origen INT NOT NULL,
    id_repartidor INT NULL,

    descripcion_paquete VARCHAR(150) NOT NULL,
    fecha_hora_estimada DATETIME NOT NULL,

    estado VARCHAR(30) NOT NULL DEFAULT 'Pendiente de recolección',
    motivo_rechazo VARCHAR(255),

    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    fecha_modificacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP
        ON UPDATE CURRENT_TIMESTAMP,

    PRIMARY KEY (id_solicitud),

    INDEX ndx_solicitud_cliente (id_cliente),
    INDEX ndx_solicitud_estado (estado),

    CONSTRAINT fk_solicitud_cliente
        FOREIGN KEY (id_cliente)
        REFERENCES cliente(id_cliente),

    CONSTRAINT fk_solicitud_direccion_origen
        FOREIGN KEY (id_direccion_origen)
        REFERENCES direccion(id_direccion),

    CONSTRAINT fk_solicitud_repartidor
        FOREIGN KEY (id_repartidor)
        REFERENCES repartidor(id_repartidor)
)
ENGINE = InnoDB;

CREATE TABLE envio (
    id_envio INT NOT NULL AUTO_INCREMENT,

    id_cliente INT NOT NULL,
    id_paquete INT NOT NULL,
    id_repartidor INT NULL,
    id_solicitud INT NULL,

    id_direccion_origen INT NOT NULL,
    id_direccion_destino INT NOT NULL,

    id_estado INT NOT NULL,

    codigo_seguimiento VARCHAR(30) NOT NULL UNIQUE,
    nombre_destinatario VARCHAR(100),
    telefono_destinatario VARCHAR(25),

    fecha_recoleccion_estimada DATETIME,
    fecha_envio DATETIME DEFAULT CURRENT_TIMESTAMP,

    observacion VARCHAR(255),

    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    fecha_modificacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP
        ON UPDATE CURRENT_TIMESTAMP,

    PRIMARY KEY (id_envio),

    UNIQUE KEY uq_envio_solicitud (id_solicitud),

    INDEX ndx_envio_cliente (id_cliente),
    INDEX ndx_envio_paquete (id_paquete),
    INDEX ndx_envio_repartidor (id_repartidor),
    INDEX ndx_envio_estado (id_estado),

    CONSTRAINT fk_envio_cliente
        FOREIGN KEY (id_cliente)
        REFERENCES cliente(id_cliente),

    CONSTRAINT fk_envio_paquete
        FOREIGN KEY (id_paquete)
        REFERENCES paquete(id_paquete),

    CONSTRAINT fk_envio_repartidor
        FOREIGN KEY (id_repartidor)
        REFERENCES repartidor(id_repartidor),

    CONSTRAINT fk_envio_solicitud
        FOREIGN KEY (id_solicitud)
        REFERENCES solicitud_recoleccion(id_solicitud),

    CONSTRAINT fk_envio_direccion_origen
        FOREIGN KEY (id_direccion_origen)
        REFERENCES direccion(id_direccion),

    CONSTRAINT fk_envio_direccion_destino
        FOREIGN KEY (id_direccion_destino)
        REFERENCES direccion(id_direccion),

    CONSTRAINT fk_envio_estado
        FOREIGN KEY (id_estado)
        REFERENCES estado_envio(id_estado)
)
ENGINE = InnoDB;



INSERT INTO rol (rol) VALUES
('ADMIN'), 
('CLIENTE'),
('REPARTIDOR');

INSERT INTO usuario(username,password,nombre,apellidos,correo,telefono,ruta_imagen,activo)VALUES
('jimena','$2a$10$P1.w58XvnaYQUQgZUCk4aO/RTRl8EValluCqB3S2VMLTbRt.tlre.','Jimena','Arrieta Hernandez','jimena@gmail.com','4556-8978',NULL,true),
('emily','$2a$10$GkEj.ZzmQa/aEfDmtLIh3udIH5fMphx/35d0EYeqZL5uzgCJ0lQRi','Emily','Leiva Vega','emily@gmail.com','5456-8789',NULL,true),
('katherine','$2a$10$koGR7eS22Pv5KdaVJKDcge04ZB53iMiw76.UjHPY.XyVYlYqXnPbO','Katherine','Gomez Soto','katherine@gmail.com','7898-8936',NULL,true);

INSERT INTO usuario_rol (id_usuario,id_rol) VALUES
(1,1),
(2,2),
(3,3);

INSERT INTO direccion(provincia,canton,distrito,direccion_exacta)VALUES
('San José','San José','Carmen','200 metros norte del Parque Morazán'),
('San José','Escazú','San Rafael','Centro Comercial Multiplaza, local 25'),
('Alajuela','Alajuela','Alajuela','100 metros sur de la Catedral de Alajuela'),
('Cartago','Cartago','Oriental','150 metros este de la Basílica de los Ángeles'),
('Heredia','Heredia','Mercedes','200 metros oeste de la Universidad Nacional'),
('Puntarenas','Puntarenas','Puntarenas','Frente al parque central de Puntarenas');

INSERT INTO cliente(id_usuario,telefono)VALUES
(2,'5456-8789');

INSERT INTO repartidor(id_usuario,vehiculo,estado,foto_url,licencia_url)VALUES
(3,'Motocicleta','Disponible',NULL,NULL);

INSERT INTO paquete(descripcion,peso,dimensiones)VALUES
('Paquete pequeño',1.50,'20x15x10 cm'),
('Paquete mediano',5.00,'40x30x20 cm'),
('Paquete grande',12.75,'60x45x35 cm'),
('Documentos',0.50,'35x25x2 cm'),
('Equipo electrónico',8.25,'50x40x25 cm');

INSERT INTO estado_envio(nombre_estado)VALUES
('Registrado'),
('En preparación'),
('En tránsito'),
('Entregado'),
('Cancelado');

INSERT INTO solicitud_recoleccion(id_cliente,id_direccion_origen,id_repartidor,descripcion_paquete,fecha_hora_estimada,estado,motivo_rechazo)VALUES
(1,1,NULL,'Caja mediana con libros de texto','2026-08-20 09:00:00','Pendiente de recolección',NULL),
(1,3,1,'Documentos legales urgentes','2026-08-14 14:00:00','Aprobada',NULL),
(1,5,NULL,'Equipo electrónico frágil','2026-08-10 10:00:00','Rechazada','Dirección fuera de la zona de cobertura'),
(1,2,NULL,'Ropa y accesorios','2026-08-09 08:00:00','Cancelada',NULL);

INSERT INTO envio(id_cliente,id_paquete,id_repartidor,id_direccion_origen,id_direccion_destino,id_estado,codigo_seguimiento,nombre_destinatario,telefono_destinatario,fecha_recoleccion_estimada,fecha_envio,observacion)VALUES
(1,1,1,1,2,1,'ENV-202608130001','Santiago Brenes','8888-1111','2026-08-14 09:00:00','2026-08-13 08:30:00','Entregar directamente al destinatario.'),
(1,2,1,1,3,3,'ENV-202608130002','Carlos Hidalgo','8888-2222','2026-08-13 10:00:00','2026-08-13 09:15:00','Paquete en tránsito.'),
(1,3,NULL,1,4,1,'ENV-202608130003','Daniela Picado','8888-3333','2026-08-15 08:00:00',NULL,'Pendiente de asignación de repartidor.'),
(1,4,1,2,5,4,'ENV-202608130004','Fatima Hernandez','8888-4444','2026-08-12 11:00:00','2026-08-12 10:30:00','Entrega realizada correctamente.');

INSERT INTO ruta(ruta,id_rol,requiere_rol)VALUES
('/usuario/**',1,TRUE),
('/usuario_rol/**',1,TRUE),
('/cliente/**',1,TRUE),
('/repartidor/**',1,TRUE),
('/paquete/**',1,TRUE),
('/direccion/**',1,TRUE),
('/envio/**',1,TRUE),
('/estado-envio/**',1,TRUE),
('/constante/**',1,TRUE),
('/ruta/**',1,TRUE);

INSERT INTO ruta(ruta,requiere_rol)VALUES
('/',FALSE),
('/index',FALSE),
('/login',FALSE),
('/registro/**',FALSE),
('/acceso_denegado',FALSE),
('/error/**',FALSE),
('/errores/**',FALSE),
('/js/**',FALSE),
('/css/**',FALSE),
('/webjars/**',FALSE),
('/images/**',FALSE);

INSERT INTO constante(atributo,valor)VALUES
('dominio','localhost'),
('servidor.http','http://localhost:8083'),
('nombre.sistema','Sistema de Gestión de Envíos'),
('estado.inicial','Registrado'),
('estado.disponible','Disponible');


USE sistemaGestionEnvios;
SELECT * FROM usuario;

SET SQL_SAFE_UPDATES = 0;

UPDATE usuario
SET ruta_imagen = 'https://assets.purewow.com/wp-content/uploads/2022/10/sofia-vergara-hub-mobile.jpg'
WHERE username = 'jimena';

UPDATE usuario
SET ruta_imagen = 'https://ella.paraguay.com/wp-content/uploads/2014/08/jessica-alba1.jpg'
WHERE username = 'emily';

UPDATE usuario
SET ruta_imagen = 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQNquROKBOY-oiVsTDgMBRy-bV4WjaxP39sYIaDcdbrZeK1O-JODI2jtFU&s=10'
WHERE username = 'katherine';
SET SQL_SAFE_UPDATES = 1;

SELECT username, ruta_imagen
FROM usuario
WHERE username IN ('jimena', 'emily', 'katherine');

INSERT IGNORE INTO usuario_rol (id_usuario, id_rol) VALUES
(1, 2),
(1, 3),
(3, 2);

DELETE FROM ruta
WHERE id_ruta > 0;

INSERT INTO ruta (ruta, id_rol, requiere_rol) VALUES
('/usuario/**', 1, TRUE),
('/usuario_rol/**', 1, TRUE),
('/constante/**', 1, TRUE),
('/ruta/**', 1, TRUE),
('/cliente/guardar', 1, TRUE),
('/cliente/eliminar', 1, TRUE),
('/cliente/modificar/**', 1, TRUE),
('/repartidor/guardar', 1, TRUE),
('/repartidor/eliminar', 1, TRUE),
('/repartidor/modificar/**', 1, TRUE),
('/paquete/guardar', 1, TRUE),
('/paquete/eliminar', 1, TRUE),
('/paquete/modificar/**', 1, TRUE),
('/direccion/guardar', 1, TRUE),
('/direccion/eliminar', 1, TRUE),
('/direccion/modificar/**', 1, TRUE),
('/envio/guardar', 1, TRUE),
('/envio/eliminar', 1, TRUE),
('/envio/modificar/**', 1, TRUE),
('/', NULL, FALSE),
('/index', NULL, FALSE),
('/login', NULL, FALSE),
('/registro/**', NULL, FALSE),
('/acceso_denegado', NULL, FALSE),
('/fav/**', NULL, FALSE),
('/error/**', NULL, FALSE),
('/errores/**', NULL, FALSE),
('/js/**', NULL, FALSE),
('/css/**', NULL, FALSE),
('/webjars/**', NULL, FALSE),
('/solicitud-recoleccion/aprobar', 1, TRUE),
('/solicitud-recoleccion/rechazar', 1, TRUE),
('/solicitud-recoleccion/eliminar', 1, TRUE),
('/images/**', NULL, FALSE);

SELECT * FROM ruta ORDER BY id_ruta;