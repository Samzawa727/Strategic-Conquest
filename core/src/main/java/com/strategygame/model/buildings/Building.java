package com.strategygame.model.buildings;

import java.util.HashMap;
import java.util.Map;

/**
 * Classe abstraite représentant un bâtiment
 */
public abstract class Building {
    protected String nom;
    protected int pv;
    protected Map<String, Integer> cout;
    protected int tempsConstruction;
    protected int tourRestant;
    protected int x;
    protected int y;
    protected boolean enConstruction;
    
    public Building(String nom, int pv, int tempsConstruction) {
        this.nom = nom;
        this.pv = pv;
        this.tempsConstruction = tempsConstruction;
        this.tourRestant = tempsConstruction;
        this.cout = new HashMap<>();
        this.enConstruction = true;
    }
    
    /**
     * Met à jour le bâtiment chaque tour
     */
    public void update() {
        if (enConstruction && tourRestant > 0) {
            tourRestant--;
            if (tourRestant <= 0) {
                enConstruction = false;
            }
        }
    }
    
    /**
     * Action spécifique du bâtiment (production, etc.)
     */
    public abstract void executerAction();
    
    public boolean estConstruit() {
        return !enConstruction;
    }
    
    public void recevoirDegats(int degats) {
        pv = Math.max(0, pv - degats);
    }
    
    public boolean estDetruit() {
        return pv <= 0;
    }
    
    // Getters
    public String getNom() { return nom; }
    public int getPv() { return pv; }
    public Map<String, Integer> getCout() { return cout; }
    public int getTempsConstruction() { return tempsConstruction; }
    public int getTourRestant() { return tourRestant; }
    public int getX() { return x; }
    public int getY() { return y; }
    public boolean isEnConstruction() { return enConstruction; }
    
    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }
    
    // Setters pour l'initialisation
    public void setTourRestant(int tours) {
        this.tourRestant = tours;
    }
    
    public void setEnConstruction(boolean enConstruction) {
        this.enConstruction = enConstruction;
    }
    
    @Override
    public String toString() {
        String etat = enConstruction ? String.format(" (Construction: %d tours)", tourRestant) : " [Actif]";
        return nom + etat + String.format(" [PV: %d]", pv);
    }
}