package com.salonia.service;

import com.salonia.dto.request.SalonRequest;
import com.salonia.dto.response.SalonResponse;

import java.util.List;

public interface SalonService {
    SalonResponse createSalon(SalonRequest request);
    List<SalonResponse> getAllSalones();
    SalonResponse getSalonById(Long id);
    SalonResponse updateSalon(Long id, SalonRequest request);
    void deleteSalon(Long id);
}
