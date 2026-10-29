package com.salonia.service;

import com.salonia.dto.request.CreateClienteRequest;
import com.salonia.dto.request.UpdateClienteRequest;
import com.salonia.dto.response.ClienteResponse;

import java.util.List;

public interface ClienteService {
    ClienteResponse createCliente(CreateClienteRequest request, Long salonId);
    List<ClienteResponse> getAllClientes(Long salonId);
    ClienteResponse getClienteById(Long id, Long salonId);
    ClienteResponse updateCliente(Long id, UpdateClienteRequest request, Long salonId);
    void deleteCliente(Long id, Long salonId);
}
