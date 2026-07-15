package com.edugest.pro.repositories;

import com.edugest.pro.models.Signalement;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

    public interface SignalementRepository extends JpaRepository<Signalement, Long> {
        List<Signalement> findAllByOrderByDateSignalementDesc();
        List<Signalement> findByEleveId(Long eleveId);
}