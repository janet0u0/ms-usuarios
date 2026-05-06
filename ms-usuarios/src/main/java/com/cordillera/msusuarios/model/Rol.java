package com.cordillera.msusuarios.model;

/**
 * Enum Rol - MS-Usuarios
 * Define los roles del sistema RBAC basados en el caso Grupo Cordillera.
 *
 * Roles:
 *  - EJECUTIVO:     Alta Gerencia - Toma decisiones estratégicas
 *  - ANALISTA:      Analista de Negocio - Analiza métricas y KPIs
 *  - SUPERVISOR:    Admin. de Datos - Centraliza y limpia información
 *  - ADMIN_SISTEMA: Admin. del Sistema - Seguridad y estabilidad técnica
 */
public enum Rol {
    EJECUTIVO("Alta Gerencia"),
    ANALISTA("Analista de Negocio"),
    SUPERVISOR("Administrador de Datos"),
    ADMIN_SISTEMA("Administrador del Sistema");

    private final String descripcion;

    Rol(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
}