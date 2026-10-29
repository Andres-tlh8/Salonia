package com.salonia.service.impl;

import com.salonia.dto.request.SalonRequest;
import com.salonia.dto.response.SalonResponse;
import com.salonia.entity.Salon;
import com.salonia.exception.EntityNotFoundException;
import com.salonia.repository.SalonRepository;
import com.salonia.service.SalonService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SalonServiceImpl implements SalonService {

    private final SalonRepository salonRepository;

    @Override
    public SalonResponse createSalon(SalonRequest request) {
        Salon salon = new Salon();
        salon.setNombre(request.nombre());
        salon.setCiudad(request.ciudad());
        salon.setDireccion(request.direccion());
        salon.setTelefono(request.telefono());
        salon.setActivo(request.activo() != null ? request.activo() : true);
        return toResponse(salonRepository.save(salon));
    }

    @Override
    public List<SalonResponse> getAllSalones() {
        return salonRepository.findAll().stream().map(this::toResponse).toList();
    }

    @Override
    public SalonResponse getSalonById(Long id) {
        return toResponse(findSalonById(id));
    }

    @Override
    public SalonResponse updateSalon(Long id, SalonRequest request) {
        Salon salon = findSalonById(id);
        salon.setNombre(request.nombre());
        salon.setCiudad(request.ciudad());
        salon.setDireccion(request.direccion());
        salon.setTelefono(request.telefono());
        salon.setActivo(request.activo() != null ? request.activo() : salon.getActivo());
        return toResponse(salonRepository.save(salon));
    }

    @Override
    public void deleteSalon(Long id) {
        salonRepository.delete(findSalonById(id));
    }

    private Salon findSalonById(Long id) {
        return salonRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Salon no encontrado con id: " + id));
    }

    private SalonResponse toResponse(Salon salon) {
        return new SalonResponse(
                salon.getId(),
                salon.getNombre(),
                salon.getCiudad(),
                salon.getDireccion(),
                salon.getTelefono(),
                salon.getActivo()
        );
    }
}
