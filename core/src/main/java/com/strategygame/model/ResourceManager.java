package com.strategygame.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Gère les ressources du joueur (Or, Bois, Pierre, Nourriture)
 */
public class ResourceManager {
    private final Map<String, Integer> ressources;
    
    public ResourceManager() {
        ressources = new HashMap<>();
        // Initialisation des ressources de départ
        ressources.put("Or", 500);
        ressources.put("Bois", 300);
        ressources.put("Pierre", 200);
        ressources.put("Nourriture", 400);
    }
    
    /**
     * Ajoute une quantité de ressource
     */
    public void ajouterRessource(String type, int quantite) {
        ressources.put(type, ressources.getOrDefault(type, 0) + quantite);
    }
    
    /**
     * Retire une quantité de ressource
     */
    public boolean retirerRessource(String type, int quantite) {
        int actuel = ressources.getOrDefault(type, 0);
        if (actuel >= quantite) {
            ressources.put(type, actuel - quantite);
            return true;
        }
        return false;
    }
    
    /**
     * Vérifie si le joueur possède assez de ressources
     */
    public boolean possede(Map<String, Integer> cout) {
        for (Map.Entry<String, Integer> entry : cout.entrySet()) {
            if (ressources.getOrDefault(entry.getKey(), 0) < entry.getValue()) {
                return false;
            }
        }
        return true;
    }
    
    /**
     * Consomme des ressources selon un coût donné
     */
    public boolean consommer(Map<String, Integer> cout) {
        if (!possede(cout)) {
            return false;
        }
        
        for (Map.Entry<String, Integer> entry : cout.entrySet()) {
            retirerRessource(entry.getKey(), entry.getValue());
        }
        return true;
    }
    
    public int getRessource(String type) {
        return ressources.getOrDefault(type, 0);
    }
    
    public Map<String, Integer> getRessources() {
        return new HashMap<>(ressources);
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Ressources: ");
        for (Map.Entry<String, Integer> entry : ressources.entrySet()) {
            sb.append(entry.getKey()).append(": ").append(entry.getValue()).append(" ");
        }
        return sb.toString();
    }
}