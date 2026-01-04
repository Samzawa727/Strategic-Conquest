package com.strategygame.model;

import com.badlogic.gdx.graphics.Color;

/**
 * Énumération des types de terrain avec leurs propriétés
 */
public enum TerrainType {
    GRASS("Herbe", true, 1.0f, new Color(0.3f, 0.7f, 0.3f, 1)),
    WATER("Eau", false, 0.0f, new Color(0.2f, 0.4f, 0.8f, 1)),
    MOUNTAIN("Montagne", false, 0.0f, new Color(0.5f, 0.5f, 0.5f, 1)),
    FOREST("Forêt", true, 0.8f, new Color(0.1f, 0.5f, 0.1f, 1)),
    DESERT("Désert", true, 1.2f, new Color(0.9f, 0.8f, 0.4f, 1));
    
    private final String nom;
    private final boolean accessible;
    private final float modificateurVitesse;
    private final Color couleur;
    
    TerrainType(String nom, boolean accessible, float modificateurVitesse, Color couleur) {
        this.nom = nom;
        this.accessible = accessible;
        this.modificateurVitesse = modificateurVitesse;
        this.couleur = couleur;
    }
    
    public String getNom() { return nom; }
    public boolean isAccessible() { return accessible; }
    public float getModificateurVitesse() { return modificateurVitesse; }
    public Color getCouleur() { return couleur; }
}