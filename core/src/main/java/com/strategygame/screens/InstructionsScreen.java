package com.strategygame.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.strategygame.StrategyGame;

/**
 * Écran affichant les instructions du jeu
 */
public class InstructionsScreen implements Screen {
    private final StrategyGame game;
    private final String[] instructions = {
        "COMMENT JOUER",
        "",
        "SOURIS:",
        "- Clic gauche : Selectionner unite/case",
        "- Clic droit : Deplacement/Action",
        "",
        "CLAVIER:",
        "- B : Construire batiment",
        "- U : Creer unite",
        "- Espace : Passer le tour",
        "- ESC : Retour au menu",
        "",
        "OBJECTIF:",
        "Detruire toutes les unites ennemies !",
        "",
        "Appuyez sur ESC pour revenir"
    };
    
    public InstructionsScreen(StrategyGame game) {
        this.game = game;
    }
    
    @Override
    public void show() {
    }
    
    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.1f, 0.1f, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        
        game.batch.begin();
        
        game.font.getData().setScale(1.2f);
        game.font.setColor(Color.WHITE);
        
        float startY = Gdx.graphics.getHeight() - 80;
        for (int i = 0; i < instructions.length; i++) {
            if (instructions[i].isEmpty()) continue;
            
            if (instructions[i].equals("COMMENT JOUER")) {
                game.font.setColor(Color.GOLD);
                game.font.getData().setScale(1.5f);
            } else if (instructions[i].endsWith(":")) {
                game.font.setColor(Color.CYAN);
                game.font.getData().setScale(1.2f);
            } else {
                game.font.setColor(Color.WHITE);
                game.font.getData().setScale(1.1f);
            }
            
            game.font.draw(game.batch, instructions[i], 100, startY - i * 35);
        }
        
        game.batch.end();
        
        if (Gdx.input.isKeyJustPressed(com.badlogic.gdx.Input.Keys.ESCAPE)) {
            game.setScreen(new MenuScreen(game));
        }
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
    
    @Override
    public void dispose() {
    }
}