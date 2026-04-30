# MS-Usuarios - Grupo Cordillera

## 📌 Descripción
Microservicio de Gestión de Usuarios y Seguridad.
Administra autenticación y control de acceso basado en roles (RBAC)
para la Plataforma de Monitoreo Inteligente del Grupo Cordillera.

## 🎯 Patrón aplicado
- **Repository Pattern**: Abstrae el acceso a la base de datos,
  permitiendo cambiar el motor de BD sin afectar la lógica de negocio.

## ⚙️ Tecnologías
- Java 17
- Spring Boot 3.5.14
- Spring Data JPA
- MySQL 8.0
- Lombok
- Maven

## 📁 Estructura del proyecto
ms-usuarios/
├── controller/   → UsuarioController (endpoints REST)
├── service/      → UsuarioService (lógica de negocio)
├── repository/   → UsuarioRepository (acceso a datos)
├── model/        → Usuario, Rol (entidades JPA)
├── dto/          → UsuarioRequestDTO, UsuarioResponseDTO
├── exception/    → ResourceNotFoundException
└── config/       → CorsConfig

## 🔐 Roles disponibles
| Rol | Descripción |
|-----|-------------|
| ADMIN | Acceso total al sistema |
| ANALISTA | Ve KPIs detallados y stock |
| SUPERVISOR | Ve KPIs operativos y alertas |

## 🌐 Endpoints disponibles
| Método | URL | Descripción |
|--------|-----|-------------|
| GET | /api/usuarios | List