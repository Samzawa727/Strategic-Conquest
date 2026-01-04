package com.strategygame.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.strategygame.StrategyGame;
import com.strategygame.controller.GameController;
import com.strategygame.model.GameMap;
import com.strategygame.model.Tile;
import com.strategygame.model.buildings.*;
import com.strategygame.model.units.*;

/**ykho g eu la flm nsepari UI w render etc drthom ga3 hna**/
public class GameScreen implements Screen {
    private final StrategyGame game;
    private final GameController controller;
    private final int TILE_SIZE = 40;
    private int selectedX = -1;
    private int selectedY = -1;
    private Unit selectedUnit = null;
    
    private enum Mode {
        NORMAL, BUILD_COMMAND_CENTER, BUILD_BARRACKS, BUILD_MINE, 
        BUILD_FARM, BUILD_SAWMILL, CREATE_SOLDIER, CREATE_ARCHER, CREATE_CAVALRY
    }
    
    private Mode currentMode = Mode.NORMAL;
    
    public GameScreen(StrategyGame game) {
        this.game = game;
        this.controller = new GameController();
    }
    
    @Override
    public void show() {
    }
    
    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.05f, 0.05f, 0.1f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        
        handleInput();
        
        // Dessiner la carte
        dessinerCarte();
        
        // Dessiner l'interface utilisateur
        dessinerUI();
        
        // Vérifier fin de partie
        if (controller.estTerminee()) {
            dessinerFinPartie();
        }
    }
    
    private void handleInput() {
        // Gestion de la souris
        if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
            int mouseX = Gdx.input.getX() / TILE_SIZE;
            int mouseY = (Gdx.graphics.getHeight() - Gdx.input.getY()) / TILE_SIZE;
            
            if (currentMode == Mode.NORMAL) {
                selectionnerCase(mouseX, mouseY);
            } else {
                construireOuCreer(mouseX, mouseY);
            }
        }
        
        if (Gdx.input.isButtonJustPressed(Input.Buttons.RIGHT) && selectedUnit != null) {
            int mouseX = Gdx.input.getX() / TILE_SIZE;
            int mouseY = (Gdx.graphics.getHeight() - Gdx.input.getY()) / TILE_SIZE;
            actionUnite(mouseX, mouseY);
        }
        
        // Touches clavier chghol jeu arcade hhh
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            controller.passerTourSuivant();
            selectedUnit = null;
            selectedX = -1;
            selectedY = -1;
        }
        
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            if (currentMode != Mode.NORMAL) {
                currentMode = Mode.NORMAL;
            } else {
                game.setScreen(new MenuScreen(game));
            }
        }
        
        // Raccourcis construction
        if (Gdx.input.isKeyJustPressed(Input.Keys.B)) {
            afficherMenuBatiments();
        }
        
        if (Gdx.input.isKeyJustPressed(Input.Keys.U)) {
            afficherMenuUnites();
        }
        
        // Sélection rapide des bâtiments
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_1)) {
            currentMode = Mode.BUILD_COMMAND_CENTER;
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_2)) {
            currentMode = Mode.BUILD_BARRACKS;
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_3)) {
            currentMode = Mode.BUILD_MINE;
        }
        
        // Sélection rapide des unités
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_4)) {
            currentMode = Mode.CREATE_SOLDIER;
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_5)) {
            currentMode = Mode.CREATE_ARCHER;
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_6)) {
            currentMode = Mode.CREATE_CAVALRY;
        }
    }
    
    private void afficherMenuBatiments() {
        // Cyclage entre les modes de construction
        switch (currentMode) {
            case NORMAL:
                currentMode = Mode.BUILD_COMMAND_CENTER;
                break;
            case BUILD_COMMAND_CENTER:
                currentMode = Mode.BUILD_BARRACKS;
                break;
            case BUILD_BARRACKS:
                currentMode = Mode.BUILD_MINE;
                break;
            case BUILD_MINE:
                currentMode = Mode.BUILD_FARM;
                break;
            case BUILD_FARM:
                currentMode = Mode.BUILD_SAWMILL;
                break;
            default:
                currentMode = Mode.NORMAL;
        }
    }
    
    private void afficherMenuUnites() {
        switch (currentMode) {
            case NORMAL:
                currentMode = Mode.CREATE_SOLDIER;
                break;
            case CREATE_SOLDIER:
                currentMode = Mode.CREATE_ARCHER;
                break;
            case CREATE_ARCHER:
                currentMode = Mode.CREATE_CAVALRY;
                break;
            default:
                currentMode = Mode.NORMAL;
        }
    }
    
    private void selectionnerCase(int x, int y) {
        GameMap map = controller.getGameMap();
        Tile tile = map.getTile(x, y);
        
        if (tile != null && tile.hasUnit()) {
            Unit unit = tile.getUnit();
            if (controller.getJoueur().getUnites().contains(unit)) {
                selectedUnit = unit;
                selectedX = x;
                selectedY = y;
            }
        } else {
            selectedUnit = null;
            selectedX = -1;
            selectedY = -1;
        }
    }
    
    private void actionUnite(int x, int y) {
        if (selectedUnit == null) return;
        
        Tile targetTile = controller.getGameMap().getTile(x, y);
        if (targetTile == null) return;
        
        // Attaquer si une unité ennemie est présente
        if (targetTile.hasUnit()) {
            Unit target = targetTile.getUnit();
            if (controller.getIA().getUnites().contains(target)) {
                controller.attaquerUnite(selectedUnit, target);
            }
        } else {
            // Déplacer
            if (controller.peutDeplacer(selectedUnit, x, y)) {
                controller.deplacerUnite(selectedUnit, x, y);
                selectedX = x;
                selectedY = y;
            }
        }
    }
    
    private void construireOuCreer(int x, int y) {
        Building building = null;
        Unit unit = null;
        
        switch (currentMode) {
            case BUILD_COMMAND_CENTER:
                building = new CommandCenter(controller.getJoueur().getResourceManager());
                break;
            case BUILD_BARRACKS:
                building = new Barracks();
                break;
            case BUILD_MINE:
                building = new ResourceBuilding("Pierre", controller.getJoueur().getResourceManager());
                break;
            case BUILD_FARM:
                building = new ResourceBuilding("Nourriture", controller.getJoueur().getResourceManager());
                break;
            case BUILD_SAWMILL:
                building = new ResourceBuilding("Bois", controller.getJoueur().getResourceManager());
                break;
            case CREATE_SOLDIER:
                unit = new Soldier();
                break;
            case CREATE_ARCHER:
                unit = new Archer();
                break;
            case CREATE_CAVALRY:
                unit = new Cavalry();
                break;
        }
        
        boolean success = false;
        if (building != null) {
            success = controller.construireBatiment(building, x, y);
        } else if (unit != null) {
            success = controller.creerUnite(unit, x, y);
        }
        
        if (success) {
            currentMode = Mode.NORMAL;
        }
    }
    
    private void dessinerCarte() {
        game.shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        
        GameMap map = controller.getGameMap();
        
        for (int x = 0; x < map.getLargeur(); x++) {
            for (int y = 0; y < map.getHauteur(); y++) {
                Tile tile = map.getTile(x, y);
                
                // Couleur du terrain
                Color color = tile.getType().getCouleur();
                game.shapeRenderer.setColor(color);
                game.shapeRenderer.rect(x * TILE_SIZE, y * TILE_SIZE, TILE_SIZE - 1, TILE_SIZE - 1);
                
                // Surligner la case sélectionnée
                if (x == selectedX && y == selectedY) {
                    game.shapeRenderer.setColor(Color.YELLOW);
                    game.shapeRenderer.rect(x * TILE_SIZE, y * TILE_SIZE, TILE_SIZE - 1, TILE_SIZE - 1);
                }
                
                // Afficher les cases accessibles pour l'unité sélectionnée
                if (selectedUnit != null && !selectedUnit.aBouge()) {
                    int distance = map.distance(selectedUnit.getX(), selectedUnit.getY(), x, y);
                    if (distance <= selectedUnit.getDeplacement() && tile.isAccessible() && !tile.hasUnit()) {
                        game.shapeRenderer.setColor(0f, 1f, 0f, 0.3f); // Vert semi-transparent
                        game.shapeRenderer.rect(x * TILE_SIZE, y * TILE_SIZE, TILE_SIZE - 1, TILE_SIZE - 1);
                    }
                }
                
                // Dessiner bâtiment avec couleurs distinctives
                if (tile.hasBuilding()) {
                    Building building = tile.getBuilding();
                    Color buildingColor = Color.BROWN;
                    
                    // Couleurs selon le type de bâtiment
                    if (building.getNom().contains("Centre")) {
                        buildingColor = new Color(0.8f, 0.6f, 0.2f, 1); // Or foncé
                    } else if (building.getNom().contains("Entraînement")) {
                        buildingColor = new Color(0.6f, 0.2f, 0.2f, 1); // Rouge foncé
                    } else if (building.getNom().equals("Mine")) {
                        buildingColor = new Color(0.4f, 0.4f, 0.4f, 1); // Gris
                    } else if (building.getNom().equals("Ferme")) {
                        buildingColor = new Color(0.4f, 0.6f, 0.2f, 1); // Vert olive
                    } else if (building.getNom().equals("Scierie")) {
                        buildingColor = new Color(0.5f, 0.3f, 0.1f, 1); // Marron bois
                    }
                    
                    game.shapeRenderer.setColor(buildingColor);
                    game.shapeRenderer.rect(x * TILE_SIZE + 5, y * TILE_SIZE + 5, TILE_SIZE - 11, TILE_SIZE - 11);
                }
                
                // Dessiner unité avec couleurs distinctives
                if (tile.hasUnit()) {
                    Unit unit = tile.getUnit();
                    Color unitColor;
                    
                    if (controller.getJoueur().getUnites().contains(unit)) {
                        // Couleurs pour les unités du joueur
                        if (unit instanceof Soldier) {
                            unitColor = new Color(0.2f, 0.4f, 1f, 1); // Bleu moyen
                        } else if (unit instanceof Archer) {
                            unitColor = new Color(0.4f, 0.8f, 1f, 1); // Bleu clair
                        } else if (unit instanceof Cavalry) {
                            unitColor = new Color(0f, 0.2f, 0.8f, 1); // Bleu foncé
                        } else {
                            unitColor = Color.BLUE;
                        }
                    } else {
                        // Couleurs pour les unités de l'IA
                        if (unit instanceof Soldier) {
                            unitColor = new Color(1f, 0.3f, 0.2f, 1); // Rouge moyen
                        } else if (unit instanceof Archer) {
                            unitColor = new Color(1f, 0.6f, 0.4f, 1); // Rouge-orange
                        } else if (unit instanceof Cavalry) {
                            unitColor = new Color(0.8f, 0f, 0f, 1); // Rouge foncé
                        } else {
                            unitColor = Color.RED;
                        }
                    }
                    
                    game.shapeRenderer.setColor(unitColor);
                    game.shapeRenderer.circle(x * TILE_SIZE + TILE_SIZE / 2f, 
                                             y * TILE_SIZE + TILE_SIZE / 2f, 
                                             TILE_SIZE / 3f);
                }
            }
        }
        
        game.shapeRenderer.end();
        
        // Dessiner les contours des cases accessibles
        if (selectedUnit != null && !selectedUnit.aBouge()) {
            game.shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
            Gdx.gl.glLineWidth(3);
            
            for (int x = 0; x < map.getLargeur(); x++) {
                for (int y = 0; y < map.getHauteur(); y++) {
                    Tile tile = map.getTile(x, y);
                    int distance = map.distance(selectedUnit.getX(), selectedUnit.getY(), x, y);
                    
                    if (distance <= selectedUnit.getDeplacement() && tile.isAccessible() && !tile.hasUnit()) {
                        game.shapeRenderer.setColor(Color.GREEN);
                        game.shapeRenderer.rect(x * TILE_SIZE, y * TILE_SIZE, TILE_SIZE - 1, TILE_SIZE - 1);
                    }
                }
            }
            
            game.shapeRenderer.end();
            Gdx.gl.glLineWidth(1);
        }
        
        // Dessiner les contours des ennemis à portée d'attaque
        if (selectedUnit != null && !selectedUnit.aAttaque()) {
            game.shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
            Gdx.gl.glLineWidth(3);
            
            for (Unit ennemi : controller.getIA().getUnites()) {
                if (selectedUnit.peutAttaquer(ennemi)) {
                    game.shapeRenderer.setColor(Color.RED);
                    float cx = ennemi.getX() * TILE_SIZE + TILE_SIZE / 2f;
                    float cy = ennemi.getY() * TILE_SIZE + TILE_SIZE / 2f;
                    game.shapeRenderer.circle(cx, cy, TILE_SIZE / 3f);
                }
            }
            
            game.shapeRenderer.end();
            Gdx.gl.glLineWidth(1);
        }
        
        // Dessiner les lettres sur les unités et bâtiments
        game.batch.begin();
        game.font.getData().setScale(0.8f);
        game.font.setColor(Color.WHITE);
        
        for (int x = 0; x < map.getLargeur(); x++) {
            for (int y = 0; y < map.getHauteur(); y++) {
                Tile tile = map.getTile(x, y);
                
                // Lettre pour les bâtiments
                if (tile.hasBuilding()) {
                    Building building = tile.getBuilding();
                    String lettre = "";
                    
                    if (building.getNom().contains("Centre")) {
                        lettre = "C";
                    } else if (building.getNom().contains("Entraînement")) {
                        lettre = "E";
                    } else if (building.getNom().equals("Mine")) {
                        lettre = "M";
                    } else if (building.getNom().equals("Ferme")) {
                        lettre = "F";
                    } else if (building.getNom().equals("Scierie")) {
                        lettre = "S";
                    }
                    
                    game.font.draw(game.batch, lettre, 
                        x * TILE_SIZE + TILE_SIZE / 2f - 5, 
                        y * TILE_SIZE + TILE_SIZE / 2f + 5);
                }
                
                // Lettre pour les unités
                if (tile.hasUnit()) {
                    Unit unit = tile.getUnit();
                    String lettre = "";
                    
                    if (unit instanceof Soldier) {
                        lettre = "S";
                    } else if (unit instanceof Archer) {
                        lettre = "A";
                    } else if (unit instanceof Cavalry) {
                        lettre = "C";
                    }
                    
                    game.font.setColor(Color.WHITE);
                    game.font.draw(game.batch, lettre, 
                        x * TILE_SIZE + TILE_SIZE / 2f - 4, 
                        y * TILE_SIZE + TILE_SIZE / 2f + 4);
                }
            }
        }
        
        game.batch.end();
    }
    
    private void dessinerUI() {
        game.batch.begin();
        
        game.font.getData().setScale(1f);
        game.font.setColor(Color.WHITE);
        
        GameMap map = controller.getGameMap();
        int uiX = map.getLargeur() * TILE_SIZE + 20;
        int uiY = Gdx.graphics.getHeight() - 20;
        
        // Tour
        game.font.draw(game.batch, "Tour: " + controller.getNumeroTour(), uiX, uiY);
        
        // Ressources
        uiY -= 30;
        game.font.setColor(Color.GOLD);
        game.font.draw(game.batch, "RESSOURCES", uiX, uiY);
        
        game.font.setColor(Color.WHITE);
        uiY -= 25;
        game.font.draw(game.batch, "Or: " + controller.getJoueur().getResourceManager().getRessource("Or"), uiX, uiY);
        uiY -= 20;
        game.font.draw(game.batch, "Bois: " + controller.getJoueur().getResourceManager().getRessource("Bois"), uiX, uiY);
        uiY -= 20;
        game.font.draw(game.batch, "Pierre: " + controller.getJoueur().getResourceManager().getRessource("Pierre"), uiX, uiY);
        uiY -= 20;
        game.font.draw(game.batch, "Nourriture: " + controller.getJoueur().getResourceManager().getRessource("Nourriture"), uiX, uiY);
        
        // Mode actuel
        uiY -= 40;
        game.font.setColor(Color.CYAN);
        game.font.draw(game.batch, "Mode: " + getModeString(), uiX, uiY);
        
        // Afficher le coût et l'utilité si en mode construction/création
        if (currentMode != Mode.NORMAL) {
            uiY -= 25;
            game.font.setColor(Color.YELLOW);
            game.font.draw(game.batch, "COUT:", uiX, uiY);
            game.font.setColor(Color.WHITE);
            game.font.getData().setScale(0.9f);
            
            String coutText = getCoutModeActuel();
            String[] lignesCout = coutText.split("\n");
            for (String ligne : lignesCout) {
                uiY -= 18;
                game.font.draw(game.batch, ligne, uiX, uiY);
            }
            
            // Afficher l'utilité
            uiY -= 25;
            game.font.setColor(Color.LIME);
            game.font.draw(game.batch, "UTILITE:", uiX, uiY);
            game.font.setColor(Color.LIGHT_GRAY);
            game.font.getData().setScale(0.85f);
            
            String utiliteText = getUtiliteModeActuel();
            String[] lignesUtilite = utiliteText.split("\n");
            for (String ligne : lignesUtilite) {
                uiY -= 16;
                game.font.draw(game.batch, ligne, uiX, uiY);
            }
            
            game.font.getData().setScale(1f);
        }
        
        // Unité sélectionnée
        if (selectedUnit != null) {
            uiY -= 40;
            game.font.setColor(Color.YELLOW);
            game.font.draw(game.batch, "UNITE SELECTIONNEE", uiX, uiY);
            game.font.setColor(Color.WHITE);
            uiY -= 25;
            game.font.draw(game.batch, selectedUnit.getNom(), uiX, uiY);
            uiY -= 20;
            game.font.draw(game.batch, "PV: " + selectedUnit.getPvActuels() + "/" + selectedUnit.getPvMax(), uiX, uiY);
            uiY -= 20;
            game.font.draw(game.batch, "ATK: " + selectedUnit.getAttaque(), uiX, uiY);
            uiY -= 20;
            game.font.draw(game.batch, "DEF: " + selectedUnit.getDefense(), uiX, uiY);
            uiY -= 20;
            String statut = selectedUnit.aBouge() ? "A bouge" : "Peut bouger";
            if (selectedUnit.aAttaque()) statut += " / A attaque";
            game.font.draw(game.batch, statut, uiX, uiY);
        }
        
        // Statistiques générales
        uiY -= 40;
        game.font.setColor(Color.CYAN);
        game.font.draw(game.batch, "STATISTIQUES", uiX, uiY);
        game.font.setColor(Color.WHITE);
        uiY -= 25;
        game.font.draw(game.batch, "Vos unites: " + controller.getJoueur().getUnites().size(), uiX, uiY);
        uiY -= 20;
        game.font.draw(game.batch, "Unites IA: " + controller.getIA().getUnites().size(), uiX, uiY);
        uiY -= 20;
        game.font.draw(game.batch, "Vos batiments: " + controller.getJoueur().getBatiments().size(), uiX, uiY);
        uiY -= 20;
        game.font.draw(game.batch, "Batiments IA: " + controller.getIA().getBatiments().size(), uiX, uiY);
        
        // Notifications
        uiY -= 40;
        game.font.setColor(Color.LIGHT_GRAY);
        game.font.draw(game.batch, "NOTIFICATIONS", uiX, uiY);
        game.font.getData().setScale(0.8f);
        for (int i = 0; i < Math.min(5, controller.getNotifications().size()); i++) {
            uiY -= 20;
            game.font.draw(game.batch, controller.getNotifications().get(i), uiX, uiY);
        }
        
        // Instructions
        game.font.getData().setScale(0.9f);
        game.font.setColor(Color.GRAY);
        game.font.draw(game.batch, "S=Soldat A=Archer C=Cavalier", 10, 80);
        game.font.draw(game.batch, "Buildings: C=Centre E=Entrainement M=Mine F=Ferme S=Scierie", 10, 60);
        game.font.draw(game.batch, "B: Batiments | U: Unites | ESPACE: Fin tour", 10, 40);
        game.font.draw(game.batch, "Clic gauche: Selection | Clic droit: Action", 10, 20);
        
        game.batch.end();
    }
    
    private String getModeString() {
        switch (currentMode) {
            case BUILD_COMMAND_CENTER: return "Construire Centre";
            case BUILD_BARRACKS: return "Construire Caserne";
            case BUILD_MINE: return "Construire Mine";
            case BUILD_FARM: return "Construire Ferme";
            case BUILD_SAWMILL: return "Construire Scierie";
            case CREATE_SOLDIER: return "Creer Soldat";
            case CREATE_ARCHER: return "Creer Archer";
            case CREATE_CAVALRY: return "Creer Cavalier";
            default: return "Normal";
        }
    }
    
    private String getCoutModeActuel() {
        switch (currentMode) {
            case BUILD_COMMAND_CENTER:
                return "200 Or\n150 Bois\n100 Pierre";
            case BUILD_BARRACKS:
                return "120 Or\n80 Bois\n50 Pierre";
            case BUILD_MINE:
                return "100 Or\n50 Bois";
            case BUILD_FARM:
                return "70 Or\n40 Bois";
            case BUILD_SAWMILL:
                return "80 Or\n30 Pierre";
            case CREATE_SOLDIER:
                return "50 Or\n30 Nourriture";
            case CREATE_ARCHER:
                return "60 Or\n40 Bois\n25 Nourriture";
            case CREATE_CAVALRY:
                return "100 Or\n50 Nourriture";
            default:
                return "";
        }
    }
    
    private String getUtiliteModeActuel() {
        switch (currentMode) {
            case BUILD_COMMAND_CENTER:
                return "Batiment principal\nProduit +20 Or/tour\n500 PV\nConstruction: 3 tours";
            case BUILD_BARRACKS:
                return "Camp entrainement\nDebloque creation\nd'unites\n300 PV\nConstruction: 2 tours";
            case BUILD_MINE:
                return "Batiment economique\nProduit +15 Pierre/tour\n250 PV\nConstruction: 2 tours";
            case BUILD_FARM:
                return "Batiment economique\nProduit +15\nNourriture/tour\n250 PV\nConstruction: 2 tours";
            case BUILD_SAWMILL:
                return "Batiment economique\nProduit +15 Bois/tour\n250 PV\nConstruction: 2 tours";
            case CREATE_SOLDIER:
                return "Unite polyvalente\n100 PV | ATK: 15\nDEF: 12 | Portee: 1\nDeplacement: 3";
            case CREATE_ARCHER:
                return "Unite a distance\n70 PV | ATK: 18\nDEF: 8 | Portee: 3\nDeplacement: 2";
            case CREATE_CAVALRY:
                return "Unite rapide\n90 PV | ATK: 22\nDEF: 10 | Portee: 1\nDeplacement: 5";
            default:
                return "";
        }
    }
    
    private void dessinerFinPartie() {
        game.shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        game.shapeRenderer.setColor(0, 0, 0, 0.7f);
        game.shapeRenderer.rect(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        game.shapeRenderer.end();
        
        game.batch.begin();
        game.font.getData().setScale(2f);
        game.font.setColor(Color.GOLD);
        String message = controller.getGagnant() + " a gagne !";
        game.font.draw(game.batch, message, 
            Gdx.graphics.getWidth() / 2f - 150, 
            Gdx.graphics.getHeight() / 2f);
        
        game.font.getData().setScale(1.2f);
        game.font.setColor(Color.WHITE);
        game.font.draw(game.batch, "Appuyez sur ESC pour revenir au menu", 
            Gdx.graphics.getWidth() / 2f - 200, 
            Gdx.graphics.getHeight() / 2f - 50);
        game.batch.end();
    }
    @Override
    public void resize(int width, int height) {
    }
    @Override
    public void pause() {
    }
    
    @Override
    public void resume() {
    }
    
    @Override
    public void hide() {
    }
    
    @Override public void dispose() {}
}