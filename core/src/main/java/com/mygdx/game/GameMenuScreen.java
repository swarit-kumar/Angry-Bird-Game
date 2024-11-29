package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.mygdx.game.Birds;
import com.mygdx.game.GameLoader;
import com.mygdx.game.GameState;

public class GameMenuScreen implements Screen {
    private Stage stage;
    private SpriteBatch batch;

    public GameMenuScreen(SpriteBatch batch) {
        this.batch = batch;
        stage = new Stage(new ScreenViewport(), batch);

        // Set up UI elements
        Skin skin = new Skin(Gdx.files.internal("uiskin.json")); // Use a UI skin
        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        Label menuTitle = new Label("Game Menu", skin);
        TextButton loadButton = new TextButton("Load Game", skin);
        TextButton exitButton = new TextButton("Exit", skin);

        // Add listeners for buttons
        loadButton.addListener(new ClickListener() {
            @Override
            public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
                System.out.println("Load button clicked");
                GameState restoredState = GameLoader.loadGame("gameSave.dat");
                if (restoredState != null) {
                    restoreGameState(restoredState);
                } else {
                    System.out.println("No save file found!");
                }
            }
        });

        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
                System.out.println("Exiting...");
                Gdx.app.exit();
            }
        });

        // Add UI elements to the table
        table.add(menuTitle).colspan(2).padBottom(20);
        table.row();
        table.add(loadButton).width(200).padBottom(10);
        table.row();
        table.add(exitButton).width(200).padBottom(10);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {}

    @Override
    public void dispose() {
        stage.dispose();
    }

    private void restoreGameState(GameState gameState) {
        System.out.println("Restoring game state...");
        for (Birds bird : gameState.getBirds()) {
//            System.out.println("Restoring Bird: " + bird.getType() + " at (" + bird.getPosition() + ", " + bird.getPositionY() + ")");
        }
        // Add logic to apply the restored game state to the game world
    }
}
