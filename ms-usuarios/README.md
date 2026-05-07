# MS-Usuarios - Grupo Cordillera

Microservicio de gestión de usuarios con autenticación y control de acceso basado en roles (RBAC).

## Tecnologías
- Java 17
- Spring Boot 3.3.5
- Spring Security (BCrypt)
- Spring Data JPA
- MySQL 8.0
- Docker
- Lombok
- Maven

## Patrones Aplicados
- **Repository Pattern**: Abstrae el acceso a la base de datos
- **DTO Pattern**: Separa el modelo interno de la API
- **RBAC**: Control de acceso basado en roles (EJECUTIVO, ANALISTA, SUPERVISOR, ADMIN_SISTEMA)

## Requisitos
- Java 17
- Docker Desktop
- Maven

## Instalación y Ejecución

### 1. Clonar el repositorio
```bash
git clone <url-del-repositorio>
cd ms-usuarios
```

### 2. Levantar MySQL con Docker
```bash
docker-compose up -d
```

### 3. Ejecutar el microservicio
```bash
.\mvnw spring-boot:run
```

El servicio quedará disponible en `http://localhost:8081`

## Endpoints

### Autenticación
| Método | Endpoint | Descripción |
|--------|----------|-------------|
| POST | /api/auth/login | Login alternativo |

### Usuarios
| Método | Endpoint | Descripción |
|--------|----------|-------------|
| GET | /api/usuarios | Listar todos los usuarios |
| GET | /api/usuarios/{id} | Buscar usuario por ID |
| POST | /api/usuarios/registrar | Registrar nuevo usuario |
| POST | /api/usuarios/login | Autenticar usuario |
| PUT | /api/usuarios/{id} | Actualizar usuario |
| DELETE | /api/usuarios/{id} | Eliminar usuario |

## Ejemplo de uso

### Registrar usuario
```json
POST /api/usuarios/registrar
{
    "nombre": "Juan Pérez",
    "email": "juan@cordillera.cl",
    "password": "123456",
    "rol": "EJECUTIVO"
}
```

### Login
```json
POST /api/usuarios/login
{
    "email": "juan@cordillera.cl",
    "password": "123456"
}
```

### Roles disponibles
| Rol | Descripción |
|-----|-------------|
| EJECUTIVO | Alta Gerencia |
| ANALISTA | Analista de Negocio |
| SUPERVISOR | Administrador de Datos |
| ADMIN_SISTEMA | Administrador del Sistema |

## Estructura del proyecto
ms-usuarios/
├── src/
│   ├── main/
│   │   ├── java/com/cordillera/msusuarios/
│   │   │   ├── config/
│   │   │   │   ├── CorsConfig.java
│   │   │   │   └── SecurityConfig.java
│   │   │   ├── controller/
│   │   │   │   ├── LoginController.java
│   │   │   │   └── UsuarioController.java
│   │   │   ├── dto/
│   │   │   │   ├── UsuarioRequestDTO.java
│   │   │   │   └── UsuarioResponseDTO.java
│   │   │   ├── exception/
│   │   │   │   └── ResourceNotFoundException.java
│   │   │   ├── model/
│   │   │   │   ├── Rol.java
│   │   │   │   └── Usuario.java
│   │   │   ├── repository/
│   │   │   │   └── UsuarioRepository.java
│   │   │   └── service/
│   │   │       └── UsuarioService.java
│   │   └── resources/
│   │       └── application.properties
│   └── test/
├── docker-compose.yml
├── pom.xml
└── README.md
## Seguridad
- Passwords encriptados con BCrypt
- Roles definidos: EJECUTIVO, ANALISTA, SUPERVISOR, ADMIN_SISTEMA
- CORS configurado para localhost:4200 y localhost:3000