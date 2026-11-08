package com.salonia.enums;

public enum EstadoCita {

    PENDIENTE,
    CONFIRMADA,
    COMPLETADA,
    CANCELADA,
    NO_ASISTIO;

    public boolean ocupaAgenda() {
        return this == PENDIENTE || this == CONFIRMADA;
    }

    public boolean puedeCambiarA(EstadoCita destino) {
        if (destino == null) {
            return false;
        }

        return switch (this) {
            case PENDIENTE -> destino == CONFIRMADA || destino == CANCELADA;
            case CONFIRMADA -> destino == COMPLETADA || destino == CANCELADA || destino == NO_ASISTIO;
            case COMPLETADA, CANCELADA, NO_ASISTIO -> false;
        };
    }
}