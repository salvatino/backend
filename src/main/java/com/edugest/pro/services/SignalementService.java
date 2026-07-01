package com.edugest.pro.services;

import com.edugest.pro.models.Signalement;
import com.edugest.pro.repositories.SignalementRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class SignalementService {
    private final SignalementRepository signalementRepository;
    private final EmailService emailService;

    public SignalementService(SignalementRepository signalementRepository, EmailService emailService) {
        this.signalementRepository = signalementRepository;
        this.emailService = emailService;
    }

    public Signalement creerSignalement(Signalement s) {
        Signalement sauf = signalementRepository.save(s);
        emailService.envoyerAlerteAdministrateur(s.getDescription());
        return sauf;
    }

    public List<Signalement> recupererTousLesSignalements() {
        return signalementRepository.findAll();
    }
}