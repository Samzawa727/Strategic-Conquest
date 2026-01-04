package com.strategygame.controller;

import com.strategygame.model.*;
import com.strategygame.model.buildings.*;
import com.strategygame.model.units.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Contrôleur principal gérant la logique du jeu
 */
public class GameController {
    private final GameMap gameMap;
    private final Player joueur;
    private final Player ia;
    private Player joueurActif;
    private int numeroTour;
    private final List<String> notifications;
    
    public GameController() {
        this.gameMap = new GameMap(20, 15);
        this.joueur = new Player("Joueur", false);
        this.ia = new Player("IA", true);
        this.joueurActif = joueur;
        this.numeroTour = 1;
        this.notifications = new ArrayList<>();
        
        initialiserPartie();
    }
    
    /**
     * Initialise la partie avec des ressources et unités de départ
     */
    private void initialiserPartie() {
        // Donner des unités de départ au joueur
        ajouterUniteJoueur(new Soldier(), 2, 2);
        ajouterUniteJoueur(new Archer(), 3, 2);
        
        // Donner des unités de départ à l'IA
        ajouterUniteIA(new Soldier(), 17, 12);
        ajouterUniteIA(new Soldier(), 16, 12);
        
        // L'IA commence avec un centre de commandement DEJA CONSTRUIT
        CommandCenter ccIA = new CommandCenter(ia.getResourceManager());
        ccIA.setTourRestant(0);
        ccIA.setEnConstruction(false);
        construireBatimentIA(ccIA, 18, 13);
        
        // Donner des ressources supplémentaires à l'IA pour démarrer plus vite
        ia.getResourceManager().ajouterRessource("Or", 200);
        ia.getResourceManager().ajouterRessource("Bois", 150);
        ia.getResourceManager().ajouterRessource("Pierre", 100);
        
        ajouterNotification("Partie commencée ! Tour " + numeroTour);
    }
    
    /**
     * Passe au tour suivant
     */
    public void passerTourSuivant() {
        joueur.nouveauTour();
        ia.nouveauTour();
        
        // Nettoyer les unités mortes
        joueur.nettoyerDetruits();
        ia.nettoyerDetruits();
        
        numeroTour++;
        joueurActif = joueur;
        
        ajouterNotification("Tour " + numeroTour + " commence");
        
        // L'IA joue automatiquement
        jouerTourIA();
    }
    
    /**
     * L'IA joue son tour (comportement avancé)
     */
    private void jouerTourIA() {
        ResourceManager iaRes = ia.getResourceManager();
        
        // Phase 1: Gestion économique
        // Construire un centre de commandement si pas encore construit
        if (!ia.possedeCentreCommande() && iaRes.getRessource("Or") >= 200) {
            Tile emplacement = trouverEmplacementLibre(ia);
            if (emplacement != null) {
                CommandCenter cc = new CommandCenter(iaRes);
                if (construireBatimentIA(cc, emplacement.getX(), emplacement.getY())) {
                    ajouterNotification("IA: Centre de Commandement en construction");
                }
            }
        }
        
        // Construire une caserne si les ressources le permettent (priorité haute!)
        if (!ia.possedeCaserne() && iaRes.getRessource("Or") >= 120 && 
            iaRes.getRessource("Bois") >= 80 && iaRes.getRessource("Pierre") >= 50) {
            Tile emplacement = trouverEmplacementLibre(ia);
            if (emplacement != null) {
                Barracks barracks = new Barracks();
                if (construireBatimentIA(barracks, emplacement.getX(), emplacement.getY())) {
                    ajouterNotification("IA: Camp d'Entraînement en construction");
                }
            }
        }
        
        // Construire des bâtiments de ressources périodiquement
        if (numeroTour % 4 == 0 && iaRes.getRessource("Or") >= 80) {
            Tile emplacement = trouverEmplacementLibre(ia);
            if (emplacement != null) {
                String[] types = {"Pierre", "Bois", "Nourriture"};
                String type = types[(numeroTour / 4) % 3];
                ResourceBuilding rb = new ResourceBuilding(type, iaRes);
                
                // Vérifier si on a assez de ressources pour ce type spécifique
                if (iaRes.possede(rb.getCout()) && construireBatimentIA(rb, emplacement.getX(), emplacement.getY())) {
                    ajouterNotification("IA: " + rb.getNom() + " en construction");
                }
            }
        }
        
        // Phase 2: Production d'unités (SEULEMENT si caserne construite ET terminée)
        // Limite: Maximum 8 unités pour l'IA
        if (ia.possedeCaserne() && ia.getUnites().size() < 8) {
            // Créer des soldats si assez de ressources
            if (iaRes.getRessource("Or") >= 50 && iaRes.getRessource("Nourriture") >= 30) {
                Tile emplacement = trouverEmplacementLibreUnite(ia);
                if (emplacement != null) {
                    Soldier soldier = new Soldier();
                    if (creerUniteIA(soldier, emplacement.getX(), emplacement.getY())) {
                        ajouterNotification("IA: Soldat créé");
                    }
                }
            }
            
            // Créer des archers occasionnellement
            if (numeroTour % 3 == 0 && iaRes.getRessource("Or") >= 60 && 
                iaRes.getRessource("Bois") >= 40 && iaRes.getRessource("Nourriture") >= 25 &&
                ia.getUnites().size() < 8) {
                Tile emplacement = trouverEmplacementLibreUnite(ia);
                if (emplacement != null) {
                    Archer archer = new Archer();
                    if (creerUniteIA(archer, emplacement.getX(), emplacement.getY())) {
                        ajouterNotification("IA: Archer créé");
                    }
                }
            }
            
            // Créer des cavaliers si riche
            if (numeroTour % 5 == 0 && iaRes.getRessource("Or") >= 100 && 
                iaRes.getRessource("Nourriture") >= 50 && ia.getUnites().size() < 8) {
                Tile emplacement = trouverEmplacementLibreUnite(ia);
                if (emplacement != null) {
                    Cavalry cavalry = new Cavalry();
                    if (creerUniteIA(cavalry, emplacement.getX(), emplacement.getY())) {
                        ajouterNotification("IA: Cavalier créé");
                    }
                }
            }
        }
        
        // Phase 3: Tactique militaire
        for (Unit uniteIA : ia.getUnites()) {
            if (!uniteIA.aBouge() && !joueur.getUnites().isEmpty()) {
                // Trouver l'unité joueur la plus proche
                Unit cible = trouverCiblePlusProche(uniteIA, joueur.getUnites());
                
                if (cible != null && uniteIA.peutAttaquer(cible)) {
                    // Attaquer si à portée
                    attaquerUnite(uniteIA, cible);
                } else if (cible != null && !uniteIA.aBouge()) {
                    // Se déplacer stratégiquement
                    deplacerVers(uniteIA, cible.getX(), cible.getY());
                }
            }
        }
        
        // Phase 4: Attaquer les bâtiments ennemis si proches
        for (Unit uniteIA : ia.getUnites()) {
            if (!uniteIA.aAttaque() && !joueur.getBatiments().isEmpty()) {
                for (Building batiment : joueur.getBatiments()) {
                    int distance = gameMap.distance(uniteIA.getX(), uniteIA.getY(), 
                                                   batiment.getX(), batiment.getY());
                    if (distance <= uniteIA.getPortee()) {
                        // Attaquer le bâtiment
                        batiment.recevoirDegats(uniteIA.getAttaque());
                        uniteIA.setAAttaque(true);
                        ajouterNotification("IA attaque " + batiment.getNom() + " !");
                        break;
                    }
                }
            }
        }
    }
    
    /**
     * Trouve un emplacement libre proche du territoire de l'IA pour les bâtiments
     */
    private Tile trouverEmplacementLibre(Player player) {
        // Zone de l'IA: partie droite de la carte
        int startX = player.isIA() ? gameMap.getLargeur() * 2 / 3 : 0;
        int endX = player.isIA() ? gameMap.getLargeur() : gameMap.getLargeur() / 3;
        
        for (int tentative = 0; tentative < 50; tentative++) {
            int x = startX + (int)(Math.random() * (endX - startX));
            int y = (int)(Math.random() * gameMap.getHauteur());
            
            Tile tile = gameMap.getTile(x, y);
            if (tile != null && tile.isAccessible() && !tile.isOccupied()) {
                return tile;
            }
        }
        return null;
    }
    
    /**
     * Trouve un emplacement libre pour créer une unité (près de la caserne si possible)
     */
    private Tile trouverEmplacementLibreUnite(Player player) {
        // Zone de l'IA: partie droite de la carte
        int startX = player.isIA() ? gameMap.getLargeur() * 2 / 3 : 0;
        int endX = player.isIA() ? gameMap.getLargeur() : gameMap.getLargeur() / 3;
        
        // Essayer de trouver un emplacement proche d'une caserne
        for (Building bat : player.getBatiments()) {
            if (bat.getNom().equals("Camp d'Entraînement") && bat.estConstruit()) {
                // Chercher autour de la caserne
                for (int dx = -2; dx <= 2; dx++) {
                    for (int dy = -2; dy <= 2; dy++) {
                        int x = bat.getX() + dx;
                        int y = bat.getY() + dy;
                        Tile tile = gameMap.getTile(x, y);
                        if (tile != null && tile.isAccessible() && !tile.hasUnit()) {
                            return tile;
                        }
                    }
                }
            }
        }
        
        // Sinon, chercher n'importe où dans le territoire
        for (int tentative = 0; tentative < 50; tentative++) {
            int x = startX + (int)(Math.random() * (endX - startX));
            int y = (int)(Math.random() * gameMap.getHauteur());
            
            Tile tile = gameMap.getTile(x, y);
            if (tile != null && tile.isAccessible() && !tile.hasUnit()) {
                return tile;
            }
        }
        return null;
    }
    
    private Unit trouverCiblePlusProche(Unit unite, List<Unit> cibles) {
        Unit plusProche = null;
        int distanceMin = Integer.MAX_VALUE;
        
        for (Unit cible : cibles) {
            int distance = gameMap.distance(unite.getX(), unite.getY(), cible.getX(), cible.getY());
            if (distance < distanceMin) {
                distanceMin = distance;
                plusProche = cible;
            }
        }
        
        return plusProche;
    }
    
    private void deplacerVers(Unit unite, int targetX, int targetY) {
        int dx = Integer.compare(targetX, unite.getX());
        int dy = Integer.compare(targetY, unite.getY());
        
        int newX = unite.getX() + dx;
        int newY = unite.getY() + dy;
        
        if (peutDeplacer(unite, newX, newY)) {
            deplacerUnite(unite, newX, newY);
        }
    }
    
    /**
     * Vérifie si une unité peut se déplacer vers une position
     */
    public boolean peutDeplacer(Unit unite, int x, int y) {
        if (unite.aBouge()) return false;
        
        Tile tile = gameMap.getTile(x, y);
        if (tile == null || !tile.isAccessible()) return false;
        
        int distance = gameMap.distance(unite.getX(), unite.getY(), x, y);
        if (distance > unite.getDeplacement()) return false;
        
        // Vérifier qu'il n'y a pas d'autre unité
        return !tile.hasUnit();
    }
    
    /**
     * Déplace une unité
     */
    public void deplacerUnite(Unit unite, int x, int y) {
        Tile oldTile = gameMap.getTile(unite.getX(), unite.getY());
        Tile newTile = gameMap.getTile(x, y);
        
        if (oldTile != null) oldTile.removeUnit();
        if (newTile != null) {
            newTile.setUnit(unite);
            unite.deplacer(x, y);
            ajouterNotification(unite.getNom() + " déplacé vers (" + x + "," + y + ")");
        }
    }
    
    /**
     * Fait attaquer une unité
     */
    public void attaquerUnite(Unit attaquant, Unit cible) {
        if (!attaquant.peutAttaquer(cible)) return;
        
        int pvAvant = cible.getPvActuels();
        attaquant.attaquer(cible);
        int degats = pvAvant - cible.getPvActuels();
        
        ajouterNotification(attaquant.getNom() + " attaque " + cible.getNom() + " (-" + degats + " PV)");
        
        if (cible.estMort()) {
            Tile tile = gameMap.getTile(cible.getX(), cible.getY());
            if (tile != null) tile.removeUnit();
            ajouterNotification(cible.getNom() + " a été détruit !");
        }
    }
    
    /**
     * Construit un bâtiment pour le joueur
     */
    public boolean construireBatiment(Building batiment, int x, int y) {
        Tile tile = gameMap.getTile(x, y);
        if (tile == null || !tile.getType().isAccessible() || tile.isOccupied()) {
            return false;
        }
        
        if (!joueur.getResourceManager().consommer(batiment.getCout())) {
            ajouterNotification("Ressources insuffisantes pour construire " + batiment.getNom());
            return false;
        }
        
        batiment.setPosition(x, y);
        tile.setBuilding(batiment);
        joueur.ajouterBatiment(batiment);
        ajouterNotification(batiment.getNom() + " en construction à (" + x + "," + y + ")");
        return true;
    }
    
    /**
     * Crée une unité pour le joueur
     */
    public boolean creerUnite(Unit unite, int x, int y) {
        if (!joueur.possedeCaserne()) {
            ajouterNotification("Vous devez construire un Camp d'Entraînement d'abord !");
            return false;
        }
        
        Tile tile = gameMap.getTile(x, y);
        if (tile == null || !tile.isAccessible() || tile.hasUnit()) {
            return false;
        }
        
        if (!joueur.getResourceManager().consommer(unite.getCout())) {
            ajouterNotification("Ressources insuffisantes pour créer " + unite.getNom());
            return false;
        }
        
        ajouterUniteJoueur(unite, x, y);
        ajouterNotification(unite.getNom() + " créé à (" + x + "," + y + ")");
        return true;
    }
    
    /**
     * Crée une unité pour l'IA
     */
    private boolean creerUniteIA(Unit unite, int x, int y) {
        if (!ia.possedeCaserne()) {
            return false;
        }
        
        Tile tile = gameMap.getTile(x, y);
        if (tile == null || !tile.isAccessible() || tile.hasUnit()) {
            return false;
        }
        
        if (!ia.getResourceManager().consommer(unite.getCout())) {
            return false;
        }
        
        ajouterUniteIA(unite, x, y);
        return true;
    }
    
    /**
     * Construit un bâtiment pour l'IA
     */
    private boolean construireBatimentIA(Building batiment, int x, int y) {
        Tile tile = gameMap.getTile(x, y);
        if (tile == null || !tile.getType().isAccessible() || tile.isOccupied()) {
            return false;
        }
        
        if (!ia.getResourceManager().consommer(batiment.getCout())) {
            return false;
        }
        
        batiment.setPosition(x, y);
        tile.setBuilding(batiment);
        ia.ajouterBatiment(batiment);
        return true;
    }
    
    private void ajouterUniteJoueur(Unit unite, int x, int y) {
        unite.setPosition(x, y);
        joueur.ajouterUnite(unite);
        Tile tile = gameMap.getTile(x, y);
        if (tile != null) tile.setUnit(unite);
    }
    
    private void ajouterUniteIA(Unit unite, int x, int y) {
        unite.setPosition(x, y);
        ia.ajouterUnite(unite);
        Tile tile = gameMap.getTile(x, y);
        if (tile != null) tile.setUnit(unite);
    }
    
    /**
     * Vérifie si la partie est terminée
     */
    public boolean estTerminee() {
        return joueur.getUnites().isEmpty() || ia.getUnites().isEmpty();
    }
    
    public String getGagnant() {
        if (joueur.getUnites().isEmpty()) return "IA";
        if (ia.getUnites().isEmpty()) return "Joueur";
        return null;
    }
    
    public void ajouterNotification(String message) {
        notifications.add(0, message);
        if (notifications.size() > 10) {
            notifications.remove(notifications.size() - 1);
        }
    }
    
    // Getters
    public GameMap getGameMap() { return gameMap; }
    public Player getJoueur() { return joueur; }
    public Player getIA() { return ia; }
    public int getNumeroTour() { return numeroTour; }
    public List<String> getNotifications() { return notifications; }
}