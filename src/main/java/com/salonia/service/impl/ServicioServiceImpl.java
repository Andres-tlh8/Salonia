package com.salonia.service.impl;

import com.salonia.dto.request.CreateServicioRequest;
import com.salonia.dto.request.UpdateServicioRequest;
import com.salonia.dto.response.ServicioResponse;
import com.salonia.entity.Salon;
import com.salonia.entity.Servicio;
import com.salonia.exception.EntityNotFoundException;
import com.salonia.repository.SalonRepository;
import com.salonia.repository.ServicioRepository;
import com.salonia.service.ServicioService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ServicioServiceImpl implements ServicioService {

    private final ServicioRepository servicioRepository;
    private final SalonRepository salonRepository;

    @Override
    public ServicioResponse createServicio(CreateServicioRequest request, Long salonId) {
        Salon salon = findSalonById(salonId);
        Servicio servicio = new Servicio();
        servicio.setNombre(request.nombre());
        servicio.setDescripcion(request.descripcion());
        servicio.setDuracionMinutos(request.duracionMinutos());
        servicio.setPrecio(request.precio());
        servicio.setSalon(salon);
        return toResponse(servicioRepository.save(servicio));
    }

    @Override
    public List<ServicioResponse> getAllServicios(Long salonId) {
        findSalonById(salonId);
        return servicioRepository.findAll().stream()
                .filter(servicio -> servicio.getSalon() != null && servicio.getSalon().getId().equals(salonId))
                .map(this::toResponse)
                .toList();
    }

    @Override
    public ServicioResponse getServicioById(Long id, Long salonId) {
        Servicio servicio = servicioRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Servicio no encontrado con id: " + id));
        if (!servicio.getSalon().getId().equals(salonId)) {
            throw new EntityNotFoundException("Servicio no pertenece al salón actual");
        }
        return toResponse(servicio);
    }

    @Override
    public ServicioResponse updateServicio(Long id, UpdateServicioRequest request, Long salonId) {
        Servicio servicio = servicioRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Servicio no encontrado con id: " + id));
        if (!servicio.getSalon().getId().equals(salonId)) {
            throw new EntityNotFoundException("Servicio no pertenece al salón actual");
        }
        if (request.nombre() != null) servicio.setNombre(request.nombre());
        if (request.descripcion() != null) servicio.setDescripcion(request.descripcion());
        if (request.duracionMinutos() != null) servicio.setDuracionMinutos(request.duracionMinutos());
        if (request.precio() != null) servicio.setPrecio(request.precio());
        return toResponse(servicioRepository.save(servicio));
    }

    @Override
    public void deleteServicio(Long id, Long salonId) {
        Servicio servicio = servicioRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Servicio no encontrado con id: " + id));
        if (!servicio.getSalon().getId().equals(salonId)) {
            throw new EntityNotFoundException("Servicio no pertenece al salón actual");
        }
        servicioRepository.delete(servicio);
    }

    private Salon findSalonById(Long salonId) {
        return salonRepository.findById(salonId)
                .orElseThrow(() -> new EntityNotFoundException("Salon no encontrado con id: " + salonId));
    }

    private ServicioResponse toResponse(Servicio servicio) {
        return new ServicioResponse(
                servicio.getId(),
                servicio.getNombre(),
                servicio.getDescripcion(),
                servicio.getDuracionMinutos(),
                servicio.getPrecio(),
                servicio.getSalon().getId()
        );
    }
}
