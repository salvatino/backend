package com.edugest.pro.services;

import org.springframework.stereotype.Service;

@Service
public class EmailService {

    // On retire le JavaMailSender pour éviter le blocage au démarrage
    public EmailService() {
    }

    public void envoyerEmail(String to, String sujet, String contenu) {
        // Simulation parfaite dans la console pour votre démonstration !
        System.out.println("=================================================");
        System.out.println("📧 [SIMULATION EMAIL SENT] via EduGest Pro");
        System.out.println("À : " + to);
        System.out.println("Sujet : " + sujet);
        System.out.println("Contenu : " + contenu);
        System.out.println("=================================================");
    }
}