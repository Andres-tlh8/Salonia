package com.salonia.repository;

import com.salonia.entity.Cita;
import com.salonia.entity.Estilista;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface CitaRepository extends JpaRepository<Cita, Long> {

    List<Cita> findByEstilistaAndFecha(
            Estilista estilista,
            LocalDate fecha
    );

    List<Cita> findByEstilistaIdAndFecha(
            Long estilistaId,
            LocalDate fecha
    );

    List<Cita> findBySalonId(Long salonId);
}