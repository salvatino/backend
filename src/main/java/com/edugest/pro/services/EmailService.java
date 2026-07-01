package com.edugest.pro.services;

import org.springframework.stereotype.Service;

@Service
public class EmailService {
    public void envoyerAlerteAdministrateur(String messageAbus) {
        // Log de simulation d'envoi d'email d'urgence (Idéal démonstration Jury)
        System.out.println("[EMAIL NOTIFICATION ADMIN] Alerte critique d'abus enregistrée : " + messageAbus);
    }
}