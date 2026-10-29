package com.salonia.service;

import com.salonia.dto.request.CreateEstilistaRequest;
import com.salonia.dto.request.UpdateEstilistaRequest;
import com.salonia.dto.response.EstilistaResponse;

import java.util.List;

public interface EstilistaService {
    EstilistaResponse createEstilista(CreateEstilistaRequest request, Long salonId);
    List<EstilistaResponse> getAllEstilistas(Long salonId);
    EstilistaResponse getEstilistaById(Long id, Long salonId);
    EstilistaResponse updateEstilista(Long id, UpdateEstilistaRequest request, Long salonId);
    void deleteEstilista(Long id, Long salonId);
}
