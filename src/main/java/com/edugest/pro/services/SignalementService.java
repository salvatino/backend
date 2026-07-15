package com.edugest.pro.services;

import com.edugest.pro.models.Signalement;
import com.edugest.pro.models.enums.StatutSignalement;

import com.edugest.pro.repositories.SignalementRepository;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class SignalementService {

    private final SignalementRepository signalementRepository;
    private final EmailService emailService; // Injection du service email

    public SignalementService(SignalementRepository signalementRepository, EmailService emailService) {
        this.signalementRepository = signalementRepository;
        this.emailService = emailService;
    }

    public Signalement sauvegarder(Signalement signalement) {
        // 1. On sauvegarde d'abord en BDD
        Signalement nouveau = signalementRepository.save(signalement);
        
        // 2. On isole COMPLÈTEMENT le code d'email pour qu'il ne bloque jamais la transaction
        try {
            if (this.emailService != null) {
                emailService.envoyerEmail(
                    "admin@edugest.pro", 
                    "⚠️ ALERTE : Nouveau signalement d'incident",
                    "Un nouveau signalement de type [" + nouveau.getTypeAbus() + "] vient d'être déposé dans la cellule d'écoute. Veuillez vous connecter sur le Dashboard Admin pour auditer la situation."
                );
            }
        } catch (Throwable t) {
            // Un catch Throwable intercepte TOUT (NoClassDefFound, NullPointer, etc.)
            System.err.println("Notification email ignorée pour le développement : " + t.getMessage());
        }

        // 3. On retourne impérativement l'objet sauvegardé
        return nouveau;
    }

        // Dans SignalementService.java :
    public List<Signalement> obtenirTousLesSignalements() {
        return signalementRepository.findAllByOrderByDateSignalementDesc(); // 🔥 Utilise le tri par date !
    }


    public List<Signalement> obtenirParEleve(Long eleveId) {
        return signalementRepository.findByEleveId(eleveId);
    }

    public Signalement changerStatut(Long id, String nouveauStatutStr) {
        Signalement signalement = signalementRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Signalement introuvable avec l'id : " + id));
        
        // Conversion de la chaîne de caractères vers l'Enum
        try {
            StatutSignalement statut = StatutSignalement.valueOf(nouveauStatutStr.toUpperCase());
            signalement.setStatut(statut);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Statut invalide : " + nouveauStatutStr);
        }

        return signalementRepository.save(signalement);
    }
}