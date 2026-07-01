package com.edugest.pro.repositories;

import com.edugest.pro.models.Signalement;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SignalementRepository extends JpaRepository<Signalement, Long> {
}