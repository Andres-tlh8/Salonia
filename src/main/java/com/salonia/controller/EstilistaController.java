package com.salonia.controller;

import com.salonia.dto.request.CreateEstilistaRequest;
import com.salonia.dto.request.UpdateEstilistaRequest;
import com.salonia.dto.response.EstilistaResponse;
import com.salonia.service.EstilistaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/estilistas")
@RequiredArgsConstructor
@Tag(name = "Estilistas", description = "Gestión de estilistas por salón")
public class EstilistaController {

    private final EstilistaService estilistaService;

    @Operation(summary = "Crear estilista")
    @PreAuthorize("hasAnyRole('OWNER', 'RECEPCIONISTA', 'STYLIST')")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EstilistaResponse create(@Valid @RequestBody CreateEstilistaRequest request,
                                    @RequestParam Long salonId) {
        return estilistaService.createEstilista(request, salonId);
    }

    @Operation(summary = "Listar estilistas")
    @PreAuthorize("hasAnyRole('OWNER', 'RECEPCIONISTA', 'STYLIST')")
    @GetMapping
    public List<EstilistaResponse> getAll(@RequestParam Long salonId) {
        return estilistaService.getAllEstilistas(salonId);
    }

    @Operation(summary = "Obtener estilista por id")
    @PreAuthorize("hasAnyRole('OWNER', 'RECEPCIONISTA', 'STYLIST')")
    @GetMapping("/{id}")
    public EstilistaResponse getById(@PathVariable Long id,
                                    @RequestParam Long salonId) {
        return estilistaService.getEstilistaById(id, salonId);
    }

    @Operation(summary = "Actualizar estilista")
    @PreAuthorize("hasAnyRole('OWNER', 'RECEPCIONISTA', 'STYLIST')")
    @PutMapping("/{id}")
    public EstilistaResponse update(@PathVariable Long id,
                                   @Valid @RequestBody UpdateEstilistaRequest request,
                                   @RequestParam Long salonId) {
        return estilistaService.updateEstilista(id, request, salonId);
    }

    @Operation(summary = "Eliminar estilista")
    @PreAuthorize("hasAnyRole('OWNER', 'RECEPCIONISTA')")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id,
                       @RequestParam Long salonId) {
        estilistaService.deleteEstilista(id, salonId);
    }
}
