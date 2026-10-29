SALONIA - PROYECTO INTEGRADOR MÓDULOS 4 Y 5
Descripción General

Desarrollar una plataforma SaaS para la gestión de salones de belleza, permitiendo administrar clientes, estilistas, servicios, citas, autenticación JWT, control de roles, pagos por suscripción, créditos para IA, documentación Swagger y despliegue en la nube.

STACK TECNOLÓGICO
Backend
Java 21
Spring Boot 3
Maven
Spring Web
Spring Data JPA
Spring Validation
Spring Security
JWT
Lombok
MySQL
OpenAPI Swagger
Integraciones futuras
Stripe
Gemini API
Docker
Render
ARQUITECTURA DEL PROYECTO

Aplicar arquitectura por capas:

Plain Text
Controller
↓
Service
↓
Repository
↓
Database
 

Aplicar principios:

SOLID
Clean Code
DTO Pattern
Exception Handling Global
Security JWT
Role Based Access Control (RBAC)
ESTRUCTURA DE PAQUETES
Plain Text
com.salonia
 
├── config
├── controller
├── dto
│ ├── request
│ └── response
├── entity
├── enums
├── exception
├── repository
├── security
├── service
├── service.impl
├── util
└── SaloniaApplication
ENTIDADES
Salon
Java
Long id
String nombre
String ciudad
String direccion
String telefono
Boolean activo

Relaciones:

Plain Text
Un Salon tiene muchos Usuarios
Un Salon tiene muchos Clientes
Un Salon tiene muchos Estilistas
Un Salon tiene muchos Servicios
Un Salon tiene muchas Citas
Usuario
Java
Long id
String nombre
String email
String password
Rol rol
Salon salon

Relaciones:

Plain Text
Muchos Usuarios pertenecen a un Salon
Cliente
Java
Long id
String nombre
String telefono
String email
Salon salon
Estilista
Java
Long id
String nombre
String especialidad
Boolean activo
Salon salon
Servicio
Java
Long id
String nombre
String descripcion
Integer duracionMinutos
Double precio
Salon salon
Cita
Java
Long id
 
LocalDate fecha
 
LocalTime horaInicio
 
LocalTime horaFin
 
EstadoCita estado
 
Cliente cliente
 
Estilista estilista
 
Servicio servicio
 
Salon salon
ENUMS
Rol
Java
OWNER
RECEPCIONISTA
STYLIST
CLIENTE
EstadoCita
Java
PENDIENTE
CONFIRMADA
COMPLETADA
CANCELADA
NO_ASISTIO
REPOSITORIES

Crear interfaces JpaRepository para:

Java
SalonRepository
UsuarioRepository
ClienteRepository
EstilistaRepository
ServicioRepository
CitaRepository
 
DTOs

Generar DTO Request y Response para todas las entidades.

Salon
Java
SalonRequest
SalonResponse
Usuario
Java
RegisterRequest
LoginRequest
UsuarioResponse
Cliente
Java
CreateClienteRequest
UpdateClienteRequest
ClienteResponse
Estilista
Java
CreateEstilistaRequest
UpdateEstilistaRequest
EstilistaResponse
Servicio
Java
CreateServicioRequest
UpdateServicioRequest
ServicioResponse
Cita
Java
CreateCitaRequest
UpdateCitaRequest
CitaResponse
VALIDACIONES

Aplicar Bean Validation.

Ejemplos:

Java
@NotBlank
@NotNull
@Email
@Positive
@Size

Todos los campos obligatorios deben validarse.

EXCEPTION HANDLING

Crear:

Java
ApiError
GlobalExceptionHandler

Gestionar:

Java
MethodArgumentNotValidException
EntityNotFoundException
IllegalArgumentException
AccessDeniedException
Exception

Formato de respuesta:

JSON
{
"timestamp": "",
"status": 400,
"error": "Bad Request",
"message": "",
"path": ""
}
SPRING SECURITY Y JWT

Implementar:

Java
JwtService
JwtAuthenticationFilter
CustomUserDetailsService
SecurityConfig
AUTENTICACIÓN

Endpoints públicos:

HTTP
POST /api/auth/register
POST /api/auth/login

Respuesta Login:

JSON
{
"token": "",
"type": "Bearer"
}
AUTORIZACIÓN POR ROLES

Utilizar:

Java
@PreAuthorize

Roles:

Plain Text
OWNER
RECEPCIONISTA
STYLIST
CLIENTE
CRUD COMPLETO

Implementar CRUD REST para:

Clientes
HTTP
POST /api/clientes
GET /api/clientes
GET /api/clientes/{id}
PUT /api/clientes/{id}
DELETE /api/clientes/{id}
Estilistas
HTTP
POST /api/estilistas
GET /api/estilistas
GET /api/estilistas/{id}
PUT /api/estilistas/{id}
DELETE /api/estilistas/{id}
Servicios
HTTP
POST /api/servicios
GET /api/servicios
GET /api/servicios/{id}
PUT /api/servicios/{id}
DELETE /api/servicios/{id}
Citas
HTTP
POST /api/citas
GET /api/citas
GET /api/citas/{id}
PUT /api/citas/{id}
DELETE /api/citas/{id}
REGLAS DE NEGOCIO PARA CITAS

Implementar lógica de agenda:

Validaciones obligatorias
No permitir citas en fechas pasadas.
No permitir traslape de citas.
No permitir doble reserva del mismo estilista.
Calcular hora fin automáticamente usando la duración del servicio.
Permitir cancelar citas.
Permitir confirmar citas.
Permitir completar citas.
SWAGGER

Configurar OpenAPI.

Documentar:

Plain Text
Auth
Clientes
Estilistas
Servicios
Citas

Ruta:

Plain Text
/swagger-ui.html
BASE DE DATOS

MySQL

.properties
spring.datasource.url=jdbc:mysql://localhost:3306/salonia
spring.datasource.username=root
spring.datasource.password=root
 
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
 
server.port=8080
OBJETIVO DE LA PRIMERA FASE

Generar completamente:

✅ pom.xml

✅ application.properties

✅ Estructura de paquetes

✅ Entidades

✅ Enums

✅ Repositories

✅ DTOs

✅ Validaciones

✅ Exception Handling

✅ JWT Security

✅ Swagger

✅ CRUD Completo

✅ Agenda Básica

INSTRUCCIONES PARA LA IA

Actúa como Arquitecto Senior Java Spring Boot.

Genera el proyecto siguiendo las mejores prácticas de:

Java 21
Spring Boot 3
JPA/Hibernate
Lombok
JWT Authentication
Spring Security
DTO Pattern
REST API Design
SOLID Principles

Genera el proyecto por etapas.

Orden obligatorio:

pom.xml
application.properties
entidades
enums
repositories
DTOs
exception handling
servicios
controladores
JWT Security
Swagger
agenda de citas

No omitir ninguna clase necesaria para que el proyecto compile correctamente.