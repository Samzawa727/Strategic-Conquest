package com.strategygame.model.units;

import java.util.HashMap;
import java.util.Map;

/**
 * Classe abstraite représentant une unité militaire
 */
public abstract class Unit {
    protected String nom;
    protected int pvMax;
    protected int pvActuels;
    protected int attaque;
    protected int defense;
    protected int portee;
    protected int deplacement;
    protected Map<String, Integer> cout;
    protected int x;
    protected int y;
    protected boolean aBouge;
    protected boolean aAttaque;
    
    public Unit(String nom, int pv, int attaque, int defense, int portee, int deplacement) {
        this.nom = nom;
        this.pvMax = pv;
        this.pvActuels = pv;
        this.attaque = attaque;
        this.defense = defense;
        this.portee = portee;
        this.deplacement = deplacement;
        this.cout = new HashMap<>();
        this.aBouge = false;
        this.aAttaque = false;
    }
    
    /**
     * Attaque une autre unité
     */
    public void attaquer(Unit cible) {
        if (estMort() || cible.estMort()) {
            return;
        }
        
        // Formule de dégâts: Attaque - Défense/2 + aléatoire
        int degats = Math.max(1, this.attaque - cible.defense / 2 + (int)(Math.random() * 5));
        cible.recevoirDegats(degats);
        this.aAttaque = true;
    }
    
    /**
     * Reçoit des dégâts
     */
    public void recevoirDegats(int degats) {
        pvActuels = Math.max(0, pvActuels - degats);
    }
    
    /**
     * Soigne l'unité
     */
    public void soigner(int montant) {
        pvActuels = Math.min(pvMax, pvActuels + montant);
    }
    
    public boolean estMort() {
        return pvActuels <= 0;
    }
    
    public boolean peutAttaquer(Unit cible) {
        if (aAttaque || estMort() || cible.estMort()) {
            return false;
        }
        int distance = Math.abs(cible.x - this.x) + Math.abs(cible.y - this.y);
        return distance <= portee;
    }
    
    public void deplacer(int nouvelX, int nouvelY) {
        this.x = nouvelX;
        this.y = nouvelY;
        this.aBouge = true;
    }
    
    public void reinitialiserTour() {
        aBouge = false;
        aAttaque = false;
    }
    
    public void setAAttaque(boolean value) {
        this.aAttaque = value;
    }
    
    // Getters
    public String getNom() { return nom; }
    public int getPvMax() { return pvMax; }
    public int getPvActuels() { return pvActuels; }
    public int getAttaque() { return attaque; }
    public int getDefense() { return defense; }
    public int getPortee() { return portee; }
    public int getDeplacement() { return deplacement; }
    public Map<String, Integer> getCout() { return cout; }
    public int getX() { return x; }
    public int getY() { return y; }
    public boolean aBouge() { return aBouge; }
    public boolean aAttaque() { return aAttaque; }
    
    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }
    
    @Override
    public String toString() {
        return String.format("%s [PV: %d/%d, ATK: %d, DEF: %d]", 
            nom, pvActuels, pvMax, attaque, defense);
    }
}