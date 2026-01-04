package com.strategygame.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.strategygame.StrategyGame;

/**
 * Écran du menu principal
 */
public class MenuScreen implements Screen {
    private final StrategyGame game;
    private final String[] options = {"Nouvelle Partie", "Instructions", "Quitter"};
    private int selectedOption = 0;
    
    public MenuScreen(StrategyGame game) {
        this.game = game;
    }
    
    @Override
    public void show() {
    }
    
    @Override
    public void render(float delta) {
        // Fond bleu foncé
        Gdx.gl.glClearColor(0.1f, 0.1f, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        
        game.batch.begin();
        
        // Titre
        game.font.getData().setScale(2f);
        game.font.setColor(Color.GOLD);
        game.font.draw(game.batch, "STRATEGIC CONQUEST", 
            Gdx.graphics.getWidth() / 2f - 200, Gdx.graphics.getHeight() - 100);
        
        // Options du menu
        game.font.getData().setScale(1.5f);
        float startY = Gdx.graphics.getHeight() / 2f + 50;
        
        for (int i = 0; i < options.length; i++) {
            if (i == selectedOption) {
                game.font.setColor(Color.YELLOW);
                game.font.draw(game.batch, "> " + options[i], 
                    Gdx.graphics.getWidth() / 2f - 100, startY - i * 60);
            } else {
                game.font.setColor(Color.WHITE);
                game.font.draw(game.batch, options[i], 
                    Gdx.graphics.getWidth() / 2f - 80, startY - i * 60);
            }
        }
        
        // Instructions en bas
        game.font.getData().setScale(1f);
        game.font.setColor(Color.LIGHT_GRAY);
        game.font.draw(game.batch, "Utilisez les fleches haut/bas et Entree", 
            Gdx.graphics.getWidth() / 2f - 150, 50);
        
        game.batch.end();
        
        // Gestion des inputs
        handleInput();
    }
    
    private void handleInput() {
        if (Gdx.input.isKeyJustPressed(com.badlogic.gdx.Input.Keys.DOWN)) {
            selectedOption = (selectedOption + 1) % options.length;
        }
        
        if (Gdx.input.isKeyJustPressed(com.badlogic.gdx.Input.Keys.UP)) {
            selectedOption = (selectedOption - 1 + options.length) % options.length;
        }
        
        if (Gdx.input.isKeyJustPressed(com.badlogic.gdx.Input.Keys.ENTER)) {
            selectOption();
        }
    }
    
    private void selectOption() {
        switch (selectedOption) {
            case 0: // Nouvelle Partie
                game.setScreen(new GameScreen(game));
                break;
            case 1: // Instructions
                game.setScreen(new InstructionsScreen(game));
                break;
            case 2: // Quitter
                Gdx.app.exit();
                break;
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