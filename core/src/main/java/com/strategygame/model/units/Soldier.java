package com.strategygame.model.units;

/**
 * Unité de base : Soldat
 * Polyvalent avec statistiques équilibrées
 */
public class Soldier extends Unit {
    public Soldier() {
        super("Soldat", 100, 15, 12, 1, 3);
        
        // Définir le coût
        cout.put("Or", 50);
        cout.put("Nourriture", 30);
    }
}