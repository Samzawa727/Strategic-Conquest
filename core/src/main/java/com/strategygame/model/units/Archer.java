package com.strategygame.model.units;

/**
 * Unité à distance : Archer
 * Faible en défense mais attaque à distance
 */
public class Archer extends Unit {
    public Archer() {
        super("Archer", 70, 18, 8, 3, 2);
        
        // Définir le coût
        cout.put("Or", 60);
        cout.put("Bois", 40);
        cout.put("Nourriture", 25);
    }
}