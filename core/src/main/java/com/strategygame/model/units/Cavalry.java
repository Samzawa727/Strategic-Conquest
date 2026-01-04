package com.strategygame.model.units;

/**
 * Unité rapide : Cavalier
 * Grande mobilité et attaque puissante
 */
public class Cavalry extends Unit {
    public Cavalry() {
        super("Cavalier", 90, 22, 10, 1, 5);
        
        // Définir le coût
        cout.put("Or", 100);
        cout.put("Nourriture", 50);
    }
}