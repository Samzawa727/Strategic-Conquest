package com.strategygame.model;

import com.strategygame.model.buildings.Building;
import com.strategygame.model.units.Unit;

/**
 * Représente une case de la carte de jeu
 */
public class Tile {
    private final int x;
    private final int y;
    private final TerrainType type;
    private Unit unit;
    private Building building;
    
    public Tile(int x, int y, TerrainType type) {
        this.x = x;
        this.y = y;
        this.type = type;
    }
    
    public int getX() { return x; }
    public int getY() { return y; }
    public TerrainType getType() { return type; }
    
    public boolean hasUnit() { return unit != null; }
    public Unit getUnit() { return unit; }
    public void setUnit(Unit unit) { this.unit = unit; }
    public void removeUnit() { this.unit = null; }
    
    public boolean hasBuilding() { return building != null; }
    public Building getBuilding() { return building; }
    public void setBuilding(Building building) { this.building = building; }
    
    public boolean isAccessible() {
        return type.isAccessible() && !hasBuilding();
    }
    
    public boolean isOccupied() {
        return hasUnit() || hasBuilding();
    }
    
    @Override
    public String toString() {
        return String.format("Tile(%d,%d)[%s]", x, y, type.getNom());
    }
}