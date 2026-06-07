
markdown# 👤 MS-Usuarios - Grupo Cordillera

Microservicio de gestión de usuarios con autenticación y control de acceso basado en roles (RBAC).

## 🛠️ Tecnologías
- Java 17
- Spring Boot 3.3.5
- Spring Security (BCrypt)
- Spring Data JPA
- MySQL 8.0
- Docker
- Lombok
- Maven

## 🎯 Patrones Aplicados
- **Repository Pattern**: Abstrae el acceso a la base de datos
- **DTO Pattern**: Separa el modelo interno de la API
- **RBAC**: Control de acceso basado en roles

## ✅ Requisitos
- Java 17
- Docker Desktop
- Maven

## 🚀 Instalación y Ejecución

### Opción 1: Docker (recomendado)
```bash
docker compose up --build
```

### Opción 2: Local

**1. Clonar el repositorio**
```bash
git clone https://github.com/janet0u0/ms-usuarios
cd ms-usuarios
```

**2. Levantar MySQL con Docker**
```bash
docker-compose up -d
```

**3. Ejecutar el microservicio**
```bash
.\mvnw spring-boot:run
```
Disponible en `http://localhost:8081`

## 🔗 Endpoints

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| POST | /api/usuarios/login | Autenticar usuario |
| POST | /api/usuarios/registrar | Registrar nuevo usuario |
| GET | /api/usuarios | Listar todos los usuarios |
| GET | /api/usuarios/{id} | Buscar usuario por ID |
| PUT | /api/usuarios/{id} | Actualizar usuario |
| DELETE | /api/usuarios/{id} | Eliminar usuario |

## 📝 Ejemplo de uso

**Registrar usuario**
```json
POST /api/usuarios/registrar
{
    "nombre": "Juan Pérez",
    "email": "juan@cordillera.cl",
    "password": "123456",
    "rol": "EJECUTIVO"
}
```

**Login**
```json
POST /api/usuarios/login
{
    "email": "juan@cordillera.cl",
    "password": "123456"
}
```

## 👥 Roles disponibles

| Rol | Descripción |
|-----|-------------|
| EJECUTIVO | Alta Gerencia |
| ANALISTA | Analista de Negocio |
| SUPERVISOR | Supervisor de Operaciones |
| ADMIN_SISTEMA | Administrador del Sistema |

## 🔒 Seguridad
- Passwords encriptados con BCrypt
- CORS configurado para localhost:3000

## 📂 Estructura del Proyecto

```text
ms-usuarios/
├── src/
│   ├── main/
│   │   ├── config/
│   │   ├── controller/
│   │   ├── dto/
│   │   ├── exception/
│   │   ├── model/
│   │   ├── repository/
│   │   ├── service/
│   │   └── resources/
│   └── test/
├── docker-compose.yml
├── Dockerfile
├── pom.xml
└── README.md
```

## 📌 Componentes principales

```text
config/       → Seguridad y CORS
controller/   → Endpoints REST
dto/          → Transferencia de datos
exception/    → Manejo de errores
model/        → Entidades JPA
repository/   → Acceso a base de datos
service/      → Lógica de negocio
resources/    → Configuración
```

## 🏗️ Flujo del Microservicio

```text
Cliente → Controller → Service → Repository → MySQL
```

## 📡 Monitoreo

```
GET http://localhost:8081/actuator/health
GET http://localhost:8081/actuator/info
```
