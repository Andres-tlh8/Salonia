package com.salonia.controller;

import com.salonia.dto.request.SalonRequest;
import com.salonia.dto.response.SalonResponse;
import com.salonia.service.SalonService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/salones")
@RequiredArgsConstructor
@Tag(name = "Salones", description = "Gestión de salones")
public class SalonController {

    private final SalonService salonService;

    @Operation(summary = "Crear salón")
    @PreAuthorize("hasAnyRole('OWNER', 'RECEPCIONISTA')")
    @PostMapping
    public ResponseEntity<SalonResponse> create(@Valid @RequestBody SalonRequest request) {
        return ResponseEntity.ok(salonService.createSalon(request));
    }

    @Operation(summary = "Listar salones")
    @PreAuthorize("hasAnyRole('OWNER', 'RECEPCIONISTA')")
    @GetMapping
    public ResponseEntity<List<SalonResponse>> getAll() {
        return ResponseEntity.ok(salonService.getAllSalones());
    }

    @Operation(summary = "Obtener salón")
    @PreAuthorize("hasAnyRole('OWNER', 'RECEPCIONISTA')")
    @GetMapping("/{id}")
    public ResponseEntity<SalonResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(salonService.getSalonById(id));
    }

    @Operation(summary = "Actualizar salón")
    @PreAuthorize("hasAnyRole('OWNER', 'RECEPCIONISTA')")
    @PutMapping("/{id}")
    public ResponseEntity<SalonResponse> update(@PathVariable Long id, @Valid @RequestBody SalonRequest request) {
        return ResponseEntity.ok(salonService.updateSalon(id, request));
    }

    @Operation(summary = "Eliminar salón")
    @PreAuthorize("hasRole('OWNER')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        salonService.deleteSalon(id);
        return ResponseEntity.noContent().build();
    }
}
