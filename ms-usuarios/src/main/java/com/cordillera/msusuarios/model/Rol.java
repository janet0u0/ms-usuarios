package com.cordillera.msusuarios.model;

/**
 * Enum Rol - MS-Usuarios
 * Define los roles del sistema RBAC (Control de Acceso Basado en Roles).
 * Cada rol determina qué información puede ver el usuario en el dashboard:
 *  - ADMIN:      Acceso total al sistema
 *  - ANALISTA:   Ve KPIs detallados y stock
 *  - SUPERVISOR: Ve KPIs operativos y alertas
 */
public enum Rol {
    ADMIN("Administrador"),
    ANALISTA("Analista"),
    SUPERVISOR("Supervisor");

    private final String descripcion;

    Rol(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
}