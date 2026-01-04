package com.strategygame.model;

import com.strategygame.model.buildings.Building;
import com.strategygame.model.units.Unit;

import java.util.ArrayList;
import java.util.List;

/**
 * Représente un joueur (humain ou IA)
 */
public class Player {
    private final String nom;
    private final ResourceManager resourceManager;
    private final List<Unit> unites;
    private final List<Building> batiments;
    private final boolean estIA;
    
    public Player(String nom, boolean estIA) {
        this.nom = nom;
        this.estIA = estIA;
        this.resourceManager = new ResourceManager();
        this.unites = new ArrayList<>();
        this.batiments = new ArrayList<>();
    }
    
    public void ajouterUnite(Unit unite) {
        unites.add(unite);
    }
    
    public void retirerUnite(Unit unite) {
        unites.remove(unite);
    }
    
    public void ajouterBatiment(Building batiment) {
        batiments.add(batiment);
    }
    
    public void retirerBatiment(Building batiment) {
        batiments.remove(batiment);
    }
    
    /**
     * Réinitialise les actions de toutes les unités pour le nouveau tour
     */
    public void nouveauTour() {
        for (Unit unite : unites) {
            unite.reinitialiserTour();
        }
        
        // Mettre à jour les bâtiments
        for (Building batiment : batiments) {
            batiment.update();
            if (batiment.estConstruit()) {
                batiment.executerAction();
            }
        }
    }
    
    /**
     * Nettoie les unités mortes et bâtiments détruits
     */
    public void nettoyerDetruits() {
        unites.removeIf(Unit::estMort);
        batiments.removeIf(Building::estDetruit);
    }
    
    public String getNom() { return nom; }
    public ResourceManager getResourceManager() { return resourceManager; }
    public List<Unit> getUnites() { return unites; }
    public List<Building> getBatiments() { return batiments; }
    public boolean isIA() { return estIA; }
    
    public boolean possedeCaserne() {
        return batiments.stream()
            .anyMatch(b -> b.getNom().equals("Camp d'Entraînement") && b.estConstruit());
    }
    
    public boolean possedeCentreCommande() {
        return batiments.stream()
            .anyMatch(b -> b.getNom().equals("Centre de Commandement") && b.estConstruit());
    }
    
    @Override
    public String toString() {
        return String.format("%s - Unités: %d, Bâtiments: %d", 
            nom, unites.size(), batiments.size());
    }
}