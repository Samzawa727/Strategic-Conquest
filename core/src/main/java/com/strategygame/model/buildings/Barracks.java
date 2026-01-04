package com.strategygame.model.buildings;

/**
 * Camp d'entraînement (Caserne)
 * Permet de créer des unités militaires
 */
public class Barracks extends Building {
    public Barracks() {
        super("Camp d'Entraînement", 300, 2);
        
        // Définir le coût
        cout.put("Or", 120);
        cout.put("Bois", 80);
        cout.put("Pierre", 50);
    }
    
    @Override
    public void executerAction() {
        // La création d'unités est gérée par le GameController
        // Ce bâtiment débloque simplement cette possibilité
    }
}