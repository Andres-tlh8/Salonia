package com.salonia.service.impl;

import com.salonia.dto.request.CreateClienteRequest;
import com.salonia.dto.request.UpdateClienteRequest;
import com.salonia.dto.response.ClienteResponse;
import com.salonia.entity.Cliente;
import com.salonia.entity.Salon;
import com.salonia.exception.EntityNotFoundException;
import com.salonia.repository.ClienteRepository;
import com.salonia.repository.SalonRepository;
import com.salonia.service.ClienteService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClienteServiceImpl implements ClienteService {

    private final ClienteRepository clienteRepository;
    private final SalonRepository salonRepository;

    @Override
    public ClienteResponse createCliente(CreateClienteRequest request, Long salonId) {
        Salon salon = findSalonById(salonId);
        Cliente cliente = new Cliente();
        cliente.setNombre(request.nombre());
        cliente.setTelefono(request.telefono());
        cliente.setEmail(request.email());
        cliente.setSalon(salon);
        return toResponse(clienteRepository.save(cliente));
    }

    @Override
    public List<ClienteResponse> getAllClientes(Long salonId) {
        findSalonById(salonId);
        return clienteRepository.findAll().stream()
                .filter(cliente -> cliente.getSalon() != null && cliente.getSalon().getId().equals(salonId))
                .map(this::toResponse)
                .toList();
    }

    @Override
    public ClienteResponse getClienteById(Long id, Long salonId) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cliente no encontrado con id: " + id));
        if (!cliente.getSalon().getId().equals(salonId)) {
            throw new EntityNotFoundException("Cliente no pertenece al salón actual");
        }
        return toResponse(cliente);
    }

    @Override
    public ClienteResponse updateCliente(Long id, UpdateClienteRequest request, Long salonId) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cliente no encontrado con id: " + id));
        if (!cliente.getSalon().getId().equals(salonId)) {
            throw new EntityNotFoundException("Cliente no pertenece al salón actual");
        }
        if (request.nombre() != null) cliente.setNombre(request.nombre());
        if (request.telefono() != null) cliente.setTelefono(request.telefono());
        if (request.email() != null) cliente.setEmail(request.email());
        return toResponse(clienteRepository.save(cliente));
    }

    @Override
    public void deleteCliente(Long id, Long salonId) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cliente no encontrado con id: " + id));
        if (!cliente.getSalon().getId().equals(salonId)) {
            throw new EntityNotFoundException("Cliente no pertenece al salón actual");
        }
        clienteRepository.delete(cliente);
    }

    private Salon findSalonById(Long salonId) {
        return salonRepository.findById(salonId)
                .orElseThrow(() -> new EntityNotFoundException("Salon no encontrado con id: " + salonId));
    }

    private ClienteResponse toResponse(Cliente cliente) {
        return new ClienteResponse(
                cliente.getId(),
                cliente.getNombre(),
                cliente.getTelefono(),
                cliente.getEmail(),
                cliente.getSalon().getId()
        );
    }
}
