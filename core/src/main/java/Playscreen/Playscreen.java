package Playscreen;

import Menu.Menuscreen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.mygdx.game.Main;
import helper.Gameinfo;
import Menu.Menuscreen; // Import the new MenuScreen class

public class Playscreen implements Screen {

    private Main game;
    private Texture backgroundTexture;

    // Birds
    private Texture birdTexture1, birdTexture2, birdTexture3;

    // Catapult
    private Texture catapultTexture;

    // Blocks (Materials)
    private Texture woodTexture, glassTexture, steelTexture, woodTexture2;

    // Pigs
    private Texture smallPigTexture, mediumPigTexture, largePigTexture;
    private Texture menuTexture;
    private ImageButton menuButton;
    private Stage stage;

    public Playscreen(Main game, int level) {
        this.game = game;
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        // Load textures
        backgroundTexture = new Texture(Gdx.files.internal("background.jpeg"));
        birdTexture1 = new Texture(Gdx.files.internal("redbird.png"));
        birdTexture2 = new Texture(Gdx.files.internal("yellowbird.png"));
        birdTexture3 = new Texture(Gdx.files.internal("blackbird.png"));

        catapultTexture = new Texture(Gdx.files.internal("catapult.png"));

        woodTexture = new Texture(Gdx.files.internal("woodhorizontal.png"));
        woodTexture2 = new Texture(Gdx.files.internal("woodvertical.png"));
        glassTexture = new Texture(Gdx.files.internal("glassblock.png"));
        steelTexture = new Texture(Gdx.files.internal("stoneblock.png"));

        smallPigTexture = new Texture(Gdx.files.internal("pig.png"));
        mediumPigTexture = new Texture(Gdx.files.internal("pig - Copy.png"));
        largePigTexture = new Texture(Gdx.files.internal("pig.png"));

        menuTexture = new Texture(Gdx.files.internal("menu.png"));
        menuButton = new ImageButton(new TextureRegionDrawable(menuTexture));
        menuButton.setPosition(10, Gdx.graphics.getHeight() - 60); // Set position for the button
        menuButton.setSize(50, 50); // Set size for the button

        // Add listener to the menu button
        menuButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                // Switch to MenuScreen when the button is clicked
                game.setScreen(new Menuscreen(game));
            }
        });

        // Add the menu button to the stage
        stage.addActor(menuButton);

        // Set positions and sizes based on the level
        if (level == 1) {
            setupLevel1();
        } else if (level == 2) {
            setupLevel2();
        } else if (level == 3) {
            setupLevel3();
        }
    }

    private void setupLevel1() {
        // Set positions and sizes for level 1
    }

    private void setupLevel2() {
        // Set positions and sizes for level 2
    }

    private void setupLevel3() {
        // Set positions and sizes for level 3
    }

    @Override
    public void show() {}

    @Override
    public void render(float delta) {
        // Clear the screen
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Begin drawing with the SpriteBatch
        game.getBatch().begin();

        // Draw the background
        game.getBatch().draw(backgroundTexture, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        // Draw the birds
        game.getBatch().draw(birdTexture1, 100, 80, 40, 40);
        game.getBatch().draw(birdTexture2, 160, 80, 40, 40);
        game.getBatch().draw(birdTexture3, 220, 80, 50, 50);

        // Draw the catapult
        game.getBatch().draw(catapultTexture, 280, 80, 60, 70);

        // Draw the blocks
        game.getBatch().draw(woodTexture2, 800, 80, 170, 80);
        game.getBatch().draw(glassTexture, 720, 80, 100, 80);
        game.getBatch().draw(steelTexture, 730, 115, 170, 100);

        // Draw the pigs
        game.getBatch().draw(smallPigTexture, 810, 80, 40, 40);
        game.getBatch().draw(smallPigTexture, 790, 160, 70, 70);


        // End drawing
        game.getBatch().end();

        // Draw the stage (for the button)
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
        // Dispose textures and stage
        stage.dispose();
        backgroundTexture.dispose();
        birdTexture1.dispose();
        birdTexture2.dispose();
        birdTexture3.dispose();
        catapultTexture.dispose();
        woodTexture.dispose();
        glassTexture.dispose();
        steelTexture.dispose();
        smallPigTexture.dispose();
        mediumPigTexture.dispose();
        largePigTexture.dispose();
        menuTexture.dispose();
    }
}
