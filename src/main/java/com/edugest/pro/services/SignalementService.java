package com.edugest.pro.services;

import com.edugest.pro.models.Signalement;
import com.edugest.pro.repositories.SignalementRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class SignalementService {

    private final SignalementRepository signalementRepository;

    public SignalementService(SignalementRepository signalementRepository) {
        this.signalementRepository = signalementRepository;
    }

    // Sauvegarder un nouveau signalement
    public Signalement sauvegarder(Signalement signalement) {
        return signalementRepository.save(signalement);
    }

    // Récupérer tous les signalements (en appliquant le filtre d'anonymat)
    public List<Signalement> obtenirTousLesSignalements() {
        List<Signalement> liste = signalementRepository.findAll();
        
        // Sécurité : si le signalement est marqué anonyme, on coupe le lien avec l'élève dans le JSON
        liste.forEach(s -> {
            if (s.isAnonyme()) {
                s.setEleve(null);
            }
        });
        
        return liste;
    }
}