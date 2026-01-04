package com.strategygame.model.buildings;

import com.strategygame.model.ResourceManager;

/**
 * Centre de commandement - Bâtiment principal
 * Génère de l'or chaque tour
 */
public class CommandCenter extends Building {
    private ResourceManager resourceManager;
    private final int productionOr = 20;
    
    public CommandCenter(ResourceManager resourceManager) {
        super("Centre de Commandement", 500, 3);
        this.resourceManager = resourceManager;
        
        // Définir le coût
        cout.put("Or", 200);
        cout.put("Bois", 150);
        cout.put("Pierre", 100);
    }
    
    @Override
    public void executerAction() {
        if (estConstruit() && !estDetruit()) {
            resourceManager.ajouterRessource("Or", productionOr);
        }
    }
    
    public int getProductionOr() {
        return productionOr;
    }
    
    // Accesseurs pour modifier l'état de construction (pour l'initialisation)
    public void setTourRestant(int tours) {
        this.tourRestant = tours;
    }
    
    public void setEnConstruction(boolean enConstruction) {
        this.enConstruction = enConstruction;
    }
}