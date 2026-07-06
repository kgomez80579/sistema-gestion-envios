Entrega #1
Sistema Web de Gestión de Envíos

Integrantes del equipo

-María Jimena Arrieta Hernández (desarrolloappwebypatrones@gmail.com)
-John Isaac Alonso González (jalonso80428@ufide.ac.cr)
-Katherine de lo Ángeles Gómez Soto (kgomez80579@gmail.com)
-Emily Leiva Vega (desarrolloaplicacionescurso@gmail.com)

Descripción del proyecto
- Permitir a la empresa revisar y gestionar las solicitudes recibidas.
- Asignar repartidores a las solicitudes de recolección o entrega.
- Consultar el estado de los envíos.
- Actualizar el estado del paquete durante el proceso.
- Organizar la información de clientes, paquetes, repartidores y envíos.
- Aplicar roles de usuario para controlar el acceso a las funciones del sistema.

Usuarios del sistema

Los usuarios principales que se contemplan para el sistema son:

- Cliente
- Administrador
- Repartidor

Funcionalidades principales previstas

- Registro de usuarios en el sistema.
- Inicio de sesión según el rol del usuario.
- Gestión de clientes.
- Solicitud de recolección de paquetes por parte del cliente.
- Registro de la ubicación donde se debe recolectar el paquete.
- Registro de la dirección de entrega del paquete.
- Registro de información del paquete, como descripción, peso, tamaño o tipo.
- Revisión de solicitudes de recolección por parte de la empresa.
- Aprobación o rechazo de solicitudes según disponibilidad.
- Asignación de repartidores a solicitudes de recolección o entrega.
- Consulta del estado del envío por parte del cliente.
- Actualización del estado del paquete durante el proceso.
- Seguimiento del envío desde la solicitud hasta la entrega.
- Gestión de estados del envío, como pendiente, aprobado, recolectado, en tránsito y entregado.
- Reportes básicos de envíos realizados, pendientes o entregados.
- Manejo de roles y permisos para clientes, administradores, encargados y repartidores.

Tecnologías previstas

Para el desarrollo del proyecto se utilizarán las siguientes tecnologías:

- Java
- Spring Boot
- Thymeleaf
- Bootstrap
- Hibernate / JPA
- MySQL
- GitHub

Entrega #2
- Spring Boot se utiliza para construir la aplicación web y manejar la lógica del backend.
 
- Spring MVC permite trabajar con controladores y rutas para conectar las páginas con la lógica del sistema.
 
- Spring Data JPA junto con Hibernate permite conectar las clases Java con las tablas de MySQL. Esto permite trabajar con entidades como "Usuario", "Paquete", "Cliente", "Direccion", "Repartidor" y "Envio" sin tener que escribir consultas SQL manualmente para cada operación.
 
- MySQL se usa para almacenar toda la información del sistema.
 
- Thymeleaf se utiliza para crear las páginas dinámicas del sistema, mostrando datos reales de la base de datos.
 
- Bootstrap se usa para mejorar el diseño visual de las pantallas, formularios, tablas, botones y modales.
 
- Spring Security se usa para proteger el acceso al sistema mediante login.
 
- Firebase Storage se usa para guardar archivos externos, como imágenes de repartidores o licencias. En la base de datos no se guarda la imagen directamente, sino la URL generada por Firebase.
 
- Maven se utiliza para administrar dependencias y construir el proyecto.

Funcionalidades
domain= entidades y tablas
repository= acceso a la base de datos
service= lógica del sistema
controller= rutas y comunicación con las vistas
templates= pantallas HTML con Thymeleaf
resources= configuración, mensajes y archivos Firebase
