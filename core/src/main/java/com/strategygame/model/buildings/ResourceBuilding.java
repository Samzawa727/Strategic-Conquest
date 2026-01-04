package com.strategygame.model.buildings;

import com.strategygame.model.ResourceManager;

/**
 * Bâtiments de production de ressources (Mine, Ferme, Scierie)
 */
public class ResourceBuilding extends Building {
    private final ResourceManager resourceManager;
    private final String typeRessource;
    private final int production;
    
    public ResourceBuilding(String type, ResourceManager resourceManager) {
        super(getNomBatiment(type), 250, 2);
        this.resourceManager = resourceManager;
        this.typeRessource = type;
        this.production = 15;
        
        // Définir le coût selon le type
        switch (type) {
            case "Pierre":
                cout.put("Or", 100);
                cout.put("Bois", 50);
                break;
            case "Bois":
                cout.put("Or", 80);
                cout.put("Pierre", 30);
                break;
            case "Nourriture":
                cout.put("Or", 70);
                cout.put("Bois", 40);
                break;
        }
    }
    
    private static String getNomBatiment(String type) {
        switch (type) {
            case "Pierre": return "Mine";
            case "Bois": return "Scierie";
            case "Nourriture": return "Ferme";
            default: return "Bâtiment de Ressource";
        }
    }
    
    @Override
    public void executerAction() {
        if (estConstruit() && !estDetruit()) {
            resourceManager.ajouterRessource(typeRessource, production);
        }
    }
    
    public String getTypeRessource() { return typeRessource; }
    public int getProduction() { return production; }
}