package com.salonia.service.impl;

import com.salonia.dto.request.CreateEstilistaRequest;
import com.salonia.dto.request.UpdateEstilistaRequest;
import com.salonia.dto.response.EstilistaResponse;
import com.salonia.entity.Estilista;
import com.salonia.entity.Salon;
import com.salonia.exception.EntityNotFoundException;
import com.salonia.repository.EstilistaRepository;
import com.salonia.repository.SalonRepository;
import com.salonia.service.EstilistaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EstilistaServiceImpl implements EstilistaService {

    private final EstilistaRepository estilistaRepository;
    private final SalonRepository salonRepository;

    @Override
    public EstilistaResponse createEstilista(CreateEstilistaRequest request, Long salonId) {
        Salon salon = findSalonById(salonId);
        Estilista estilista = new Estilista();
        estilista.setNombre(request.nombre());
        estilista.setEspecialidad(request.especialidad());
        estilista.setActivo(request.activo() != null ? request.activo() : true);
        estilista.setSalon(salon);
        return toResponse(estilistaRepository.save(estilista));
    }

    @Override
    public List<EstilistaResponse> getAllEstilistas(Long salonId) {
        findSalonById(salonId);
        return estilistaRepository.findAll().stream()
                .filter(estilista -> estilista.getSalon() != null && estilista.getSalon().getId().equals(salonId))
                .map(this::toResponse)
                .toList();
    }

    @Override
    public EstilistaResponse getEstilistaById(Long id, Long salonId) {
        Estilista estilista = estilistaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Estilista no encontrado con id: " + id));
        if (!estilista.getSalon().getId().equals(salonId)) {
            throw new EntityNotFoundException("Estilista no pertenece al salón actual");
        }
        return toResponse(estilista);
    }

    @Override
    public EstilistaResponse updateEstilista(Long id, UpdateEstilistaRequest request, Long salonId) {
        Estilista estilista = estilistaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Estilista no encontrado con id: " + id));
        if (!estilista.getSalon().getId().equals(salonId)) {
            throw new EntityNotFoundException("Estilista no pertenece al salón actual");
        }
        if (request.nombre() != null) estilista.setNombre(request.nombre());
        if (request.especialidad() != null) estilista.setEspecialidad(request.especialidad());
        if (request.activo() != null) estilista.setActivo(request.activo());
        return toResponse(estilistaRepository.save(estilista));
    }

    @Override
    public void deleteEstilista(Long id, Long salonId) {
        Estilista estilista = estilistaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Estilista no encontrado con id: " + id));
        if (!estilista.getSalon().getId().equals(salonId)) {
            throw new EntityNotFoundException("Estilista no pertenece al salón actual");
        }
        estilistaRepository.delete(estilista);
    }

    private Salon findSalonById(Long salonId) {
        return salonRepository.findById(salonId)
                .orElseThrow(() -> new EntityNotFoundException("Salon no encontrado con id: " + salonId));
    }

    private EstilistaResponse toResponse(Estilista estilista) {
        return new EstilistaResponse(
                estilista.getId(),
                estilista.getNombre(),
                estilista.getEspecialidad(),
                estilista.getActivo(),
                estilista.getSalon().getId()
        );
    }
}
