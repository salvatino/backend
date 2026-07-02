package com.edugest.pro.services;

import com.edugest.pro.models.Signalement;
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
        Signalement nouveau = signalementRepository.save(signalement);
        
        // Notification automatique et instantanée par Email !
        try {
            emailService.envoyerEmail(
                "proviseur.lycee@edugest.cm", 
                "⚠️ ALERTE : Nouveau signalement d'incident",
                "Un nouveau signalement intitulé '" + signalement.getTitre() + "' vient d'être déposé dans la cellule d'écoute. Veuillez vous connecter sur le Dashboard Admin pour auditer la situation."
            );
        } catch (Exception e) {
            // Empêche le crash de l'application si le serveur mail est hors ligne
            System.err.println("Échec de l'envoi de la notification email: " + e.getMessage());
        }

        return nouveau;
    }

        public List<Signalement> obtenirTousLesSignalements() {
        return signalementRepository.findAll();
    }
    
    // ... reste de votre code

}