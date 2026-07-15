package com.edugest.pro.repositories;

import com.edugest.pro.models.Absence;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AbsenceRepository extends JpaRepository<Absence, Long> {
    List<Absence> findByEleveId(Long id); 
    // Adapte le nom selon ta relation (ex: findByEleveId)
}