// Exemple pour ClasseRepository.java
package com.edugest.pro.repositories;
import com.edugest.pro.models.Cours;
import org.springframework.data.jpa.repository.JpaRepository;
public interface CoursRepository extends JpaRepository<Cours, Long> {}