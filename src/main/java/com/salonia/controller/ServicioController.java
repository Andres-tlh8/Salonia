package com.salonia.controller;

import com.salonia.dto.request.CreateServicioRequest;
import com.salonia.dto.request.UpdateServicioRequest;
import com.salonia.dto.response.ServicioResponse;
import com.salonia.service.ServicioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/servicios")
@RequiredArgsConstructor
@Tag(name = "Servicios", description = "Gestión de servicios por salón")
public class ServicioController {

    private final ServicioService servicioService;

    @Operation(summary = "Crear servicio")
    @PreAuthorize("hasAnyRole('OWNER', 'RECEPCIONISTA', 'STYLIST')")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ServicioResponse create(@Valid @RequestBody CreateServicioRequest request,
                                  @RequestParam Long salonId) {
        return servicioService.createServicio(request, salonId);
    }

    @Operation(summary = "Listar servicios")
    @PreAuthorize("hasAnyRole('OWNER', 'RECEPCIONISTA', 'STYLIST')")
    @GetMapping
    public List<ServicioResponse> getAll(@RequestParam Long salonId) {
        return servicioService.getAllServicios(salonId);
    }

    @Operation(summary = "Obtener servicio por id")
    @PreAuthorize("hasAnyRole('OWNER', 'RECEPCIONISTA', 'STYLIST')")
    @GetMapping("/{id}")
    public ServicioResponse getById(@PathVariable Long id,
                                   @RequestParam Long salonId) {
        return servicioService.getServicioById(id, salonId);
    }

    @Operation(summary = "Actualizar servicio")
    @PreAuthorize("hasAnyRole('OWNER', 'RECEPCIONISTA', 'STYLIST')")
    @PutMapping("/{id}")
    public ServicioResponse update(@PathVariable Long id,
                                  @Valid @RequestBody UpdateServicioRequest request,
                                  @RequestParam Long salonId) {
        return servicioService.updateServicio(id, request, salonId);
    }

    @Operation(summary = "Eliminar servicio")
    @PreAuthorize("hasAnyRole('OWNER', 'RECEPCIONISTA')")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id,
                       @RequestParam Long salonId) {
        servicioService.deleteServicio(id, salonId);
    }
}
