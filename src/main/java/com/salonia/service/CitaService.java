package com.salonia.service;

import com.salonia.dto.request.CreateCitaRequest;
import com.salonia.dto.request.UpdateCitaRequest;
import com.salonia.dto.response.CitaResponse;

import java.util.List;

public interface CitaService {
    CitaResponse createCita(CreateCitaRequest request, Long salonId);
    List<CitaResponse> getAllCitas(Long salonId);
    CitaResponse getCitaById(Long id, Long salonId);
    CitaResponse updateCita(Long id, UpdateCitaRequest request, Long salonId);
    void deleteCita(Long id, Long salonId);
    CitaResponse confirmarCita(Long id, Long salonId);
    CitaResponse cancelarCita(Long id, Long salonId);
    CitaResponse completarCita(Long id, Long salonId);
}
