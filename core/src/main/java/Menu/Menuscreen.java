package Menu;

import Level.Level;
import Playscreen.PlayScreen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.mygdx.game.Main;

public class Menuscreen implements Screen {

    private Main game;
    private Stage stage;

    // Background
    private Texture backgroundTexture;
    private Image backgroundImage;

    // Buttons for Resume, Quit, and Restart Level
    private Texture resumeTexture, quitTexture, restartTexture;
    private ImageButton resumeButton, quitButton, restartButton;

    private int currentLevel; // Current level reference

    public Menuscreen(Main game,int currentLevel) {
        this.game = game;
        this.currentLevel = currentLevel;
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        // Load background texture and set it as an Image actor
        backgroundTexture = new Texture(Gdx.files.internal("background.jpeg"));
        backgroundImage = new Image(backgroundTexture);
        backgroundImage.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        stage.addActor(backgroundImage); // Add background to stage

        // Load button textures
        resumeTexture = new Texture(Gdx.files.internal("playicon.png"));
        quitTexture = new Texture(Gdx.files.internal("quit.png"));
        restartTexture = new Texture(Gdx.files.internal("restart.png")); // Updated texture for restart

        // Create ImageButtons for Resume, Quit, Restart
        resumeButton = new ImageButton(new TextureRegionDrawable(resumeTexture));
        quitButton = new ImageButton(new TextureRegionDrawable(quitTexture));
        restartButton = new ImageButton(new TextureRegionDrawable(restartTexture)); // Changed to restartButton

        // Set the positions and sizes for the buttons (center aligned)
        float buttonWidth = 200f;
        float buttonHeight = 80f;
        float centerX = (Gdx.graphics.getWidth() - buttonWidth) / 2;

        resumeButton.setSize(buttonWidth, buttonHeight);
        quitButton.setSize(buttonWidth, buttonHeight);
        restartButton.setSize(buttonWidth, buttonHeight);

        resumeButton.setPosition(centerX, Gdx.graphics.getHeight() / 2 + 120);
        restartButton.setPosition(centerX, Gdx.graphics.getHeight() / 2); // This replaces the saveButton
        quitButton.setPosition(centerX, Gdx.graphics.getHeight() / 2 - 120);

        // Add buttons to stage
        stage.addActor(resumeButton);
        stage.addActor(restartButton);
        stage.addActor(quitButton);

        // Add button listeners
        resumeButton.addListener(new com.badlogic.gdx.scenes.scene2d.utils.ChangeListener() {
            @Override
            public void changed(ChangeEvent event, com.badlogic.gdx.scenes.scene2d.Actor actor) {
                game.setScreen(new Level(game)); // Resume current PlayScreen

            }
        });

        restartButton.addListener(new com.badlogic.gdx.scenes.scene2d.utils.ChangeListener() {
            @Override
            public void changed(ChangeEvent event, com.badlogic.gdx.scenes.scene2d.Actor actor) {
                game.setScreen(new PlayScreen(game, currentLevel)); // Restart current level
                System.out.println("Restarted Level " + currentLevel);
            }
        });

        quitButton.addListener(new com.badlogic.gdx.scenes.scene2d.utils.ChangeListener() {
            @Override
            public void changed(ChangeEvent event, com.badlogic.gdx.scenes.scene2d.Actor actor) {
                Gdx.app.exit(); // Quit the game
                System.out.println("Game Exited");
            }
        });
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Render the stage (this will automatically include the background and buttons)
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void hide() {}

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void dispose() {
        stage.dispose();
        backgroundTexture.dispose();
        resumeTexture.dispose();
        quitTexture.dispose();
        restartTexture.dispose(); // Dispose of restart texture
    }
}
