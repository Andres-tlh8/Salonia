package com.salonia.controller;

import com.salonia.dto.request.CreateCitaRequest;
import com.salonia.dto.request.UpdateCitaRequest;
import com.salonia.dto.request.UpdateEstadoCitaRequest;
import com.salonia.dto.response.CitaResponse;
import com.salonia.enums.EstadoCita;
import com.salonia.service.CitaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/citas")
@RequiredArgsConstructor
@Tag(name = "Citas", description = "Gestión de citas por salón")
public class CitaController {

    private final CitaService citaService;

    @Operation(summary = "Crear cita")
    @PreAuthorize("hasAnyRole('OWNER', 'RECEPCIONISTA', 'STYLIST', 'CLIENTE')")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CitaResponse create(
            @Valid @RequestBody CreateCitaRequest request,
            @RequestParam Long salonId) {

        return citaService.createCita(request, salonId);
    }

    @Operation(summary = "Listar citas")
    @PreAuthorize("hasAnyRole('OWNER', 'RECEPCIONISTA', 'STYLIST', 'CLIENTE')")
    @GetMapping
    public List<CitaResponse> getAll(
            @RequestParam Long salonId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {

        LocalDate fechaFiltro = fecha != null ? fecha : LocalDate.now();
        return citaService.getAllCitas(salonId, fechaFiltro);
    }

    @Operation(summary = "Obtener cita por id")
    @PreAuthorize("hasAnyRole('OWNER', 'RECEPCIONISTA', 'STYLIST', 'CLIENTE')")
    @GetMapping("/{id}")
    public CitaResponse getById(
            @PathVariable Long id,
            @RequestParam Long salonId) {

        return citaService.getCitaById(id, salonId);
    }

    @Operation(summary = "Actualizar cita")
    @PreAuthorize("hasAnyRole('OWNER', 'RECEPCIONISTA', 'STYLIST', 'CLIENTE')")
    @PutMapping("/{id}")
    public CitaResponse update(
            @PathVariable Long id,
            @Valid @RequestBody UpdateCitaRequest request,
            @RequestParam Long salonId) {

        return citaService.updateCita(id, request, salonId);
    }

    @Operation(summary = "Eliminar cita")
    @PreAuthorize("hasAnyRole('OWNER', 'RECEPCIONISTA')")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(
            @PathVariable Long id,
            @RequestParam Long salonId) {

        citaService.deleteCita(id, salonId);
    }

    @Operation(summary = "Cambiar estado de cita")
    @PreAuthorize("hasAnyRole('OWNER', 'RECEPCIONISTA', 'STYLIST', 'CLIENTE')")
    @PatchMapping("/{id}/estado")
    public CitaResponse cambiarEstado(
            @PathVariable Long id,
            @Valid @RequestBody UpdateEstadoCitaRequest request,
            @RequestParam Long salonId) {

        return citaService.cambiarEstado(id, request.estado(), salonId);
    }

    @Operation(summary = "Confirmar cita")
    @PreAuthorize("hasAnyRole('OWNER', 'RECEPCIONISTA', 'STYLIST')")
    @PutMapping("/{id}/confirmar")
    public CitaResponse confirmar(
            @PathVariable Long id,
            @RequestParam Long salonId) {

        return citaService.cambiarEstado(id, EstadoCita.CONFIRMADA, salonId);
    }

    @Operation(summary = "Cancelar cita")
    @PreAuthorize("hasAnyRole('OWNER', 'RECEPCIONISTA', 'STYLIST', 'CLIENTE')")
    @PutMapping("/{id}/cancelar")
    public CitaResponse cancelar(
            @PathVariable Long id,
            @RequestParam Long salonId) {

        return citaService.cambiarEstado(id, EstadoCita.CANCELADA, salonId);
    }

    @Operation(summary = "Completar cita")
    @PreAuthorize("hasAnyRole('OWNER', 'RECEPCIONISTA', 'STYLIST')")
    @PutMapping("/{id}/completar")
    public CitaResponse completar(
            @PathVariable Long id,
            @RequestParam Long salonId) {

        return citaService.cambiarEstado(id, EstadoCita.COMPLETADA, salonId);
    }

    @Operation(summary = "Marcar cita como NO ASISTIO")
    @PreAuthorize("hasAnyRole('OWNER', 'RECEPCIONISTA', 'STYLIST')")
    @PutMapping("/{id}/no-asistio")
    public CitaResponse noAsistio(
            @PathVariable Long id,
            @RequestParam Long salonId) {

        return citaService.cambiarEstado(id, EstadoCita.NO_ASISTIO, salonId);
    }
}