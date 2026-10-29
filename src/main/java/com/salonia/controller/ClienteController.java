package com.salonia.controller;

import com.salonia.dto.request.CreateClienteRequest;
import com.salonia.dto.request.UpdateClienteRequest;
import com.salonia.dto.response.ClienteResponse;
import com.salonia.service.ClienteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clientes")
@RequiredArgsConstructor
@Tag(name = "Clientes", description = "Gestión de clientes por salón")
public class ClienteController {

    private final ClienteService clienteService;

    @Operation(summary = "Crear cliente")
    @PreAuthorize("hasAnyRole('OWNER', 'RECEPCIONISTA', 'STYLIST')")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ClienteResponse create(@Valid @RequestBody CreateClienteRequest request,
                                 @RequestParam Long salonId) {
        return clienteService.createCliente(request, salonId);
    }

    @Operation(summary = "Listar clientes")
    @PreAuthorize("hasAnyRole('OWNER', 'RECEPCIONISTA', 'STYLIST')")
    @GetMapping
    public List<ClienteResponse> getAll(@RequestParam Long salonId) {
        return clienteService.getAllClientes(salonId);
    }

    @Operation(summary = "Obtener cliente por id")
    @PreAuthorize("hasAnyRole('OWNER', 'RECEPCIONISTA', 'STYLIST')")
    @GetMapping("/{id}")
    public ClienteResponse getById(@PathVariable Long id,
                                 @RequestParam Long salonId) {
        return clienteService.getClienteById(id, salonId);
    }

    @Operation(summary = "Actualizar cliente")
    @PreAuthorize("hasAnyRole('OWNER', 'RECEPCIONISTA', 'STYLIST')")
    @PutMapping("/{id}")
    public ClienteResponse update(@PathVariable Long id,
                                 @Valid @RequestBody UpdateClienteRequest request,
                                 @RequestParam Long salonId) {
        return clienteService.updateCliente(id, request, salonId);
    }

    @Operation(summary = "Eliminar cliente")
    @PreAuthorize("hasAnyRole('OWNER', 'RECEPCIONISTA')")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id,
                       @RequestParam Long salonId) {
        clienteService.deleteCliente(id, salonId);
    }
}
