package com.strategygame.model;

import java.util.Random;

/**
 * Représente la carte de jeu composée de cases (tiles)
 */
public class GameMap {
    private final int largeur;
    private final int hauteur;
    private final Tile[][] tiles;
    
    public GameMap(int largeur, int hauteur) {
        this.largeur = largeur;
        this.hauteur = hauteur;
        this.tiles = new Tile[largeur][hauteur];
        genererCarte();
    }
    
    /**
     * Génère la carte de manière procédurale
     */
    private void genererCarte() {
        Random rand = new Random();
        
        for (int x = 0; x < largeur; x++) {
            for (int y = 0; y < hauteur; y++) {
                double valeur = rand.nextDouble();
                TerrainType type;
                
                // Distribution des terrains
                if (valeur < 0.15) {
                    type = TerrainType.WATER;
                } else if (valeur < 0.25) {
                    type = TerrainType.MOUNTAIN;
                } else if (valeur < 0.45) {
                    type = TerrainType.FOREST;
                } else if (valeur < 0.55) {
                    type = TerrainType.DESERT;
                } else {
                    type = TerrainType.GRASS;
                }
                
                tiles[x][y] = new Tile(x, y, type);
            }
        }
    }
    
    public int getLargeur() { return largeur; }
    public int getHauteur() { return hauteur; }
    
    public Tile getTile(int x, int y) {
        if (x >= 0 && x < largeur && y >= 0 && y < hauteur) {
            return tiles[x][y];
        }
        return null;
    }
    
    public boolean isValidPosition(int x, int y) {
        return x >= 0 && x < largeur && y >= 0 && y < hauteur;
    }
    
    /**
     * Calcule la distance de Manhattan entre deux positions
     */
    public int distance(int x1, int y1, int x2, int y2) {
        return Math.abs(x2 - x1) + Math.abs(y2 - y1);
    }
}