package com.cordillera.msusuarios.model;

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