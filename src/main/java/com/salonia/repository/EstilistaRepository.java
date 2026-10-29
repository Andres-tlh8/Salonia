package com.salonia.repository;

import com.salonia.entity.Estilista;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EstilistaRepository extends JpaRepository<Estilista, Long> {
}
