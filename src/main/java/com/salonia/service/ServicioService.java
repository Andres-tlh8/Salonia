package com.salonia.service;

import com.salonia.dto.request.CreateServicioRequest;
import com.salonia.dto.request.UpdateServicioRequest;
import com.salonia.dto.response.ServicioResponse;

import java.util.List;

public interface ServicioService {
    ServicioResponse createServicio(CreateServicioRequest request, Long salonId);
    List<ServicioResponse> getAllServicios(Long salonId);
    ServicioResponse getServicioById(Long id, Long salonId);
    ServicioResponse updateServicio(Long id, UpdateServicioRequest request, Long salonId);
    void deleteServicio(Long id, Long salonId);
}
