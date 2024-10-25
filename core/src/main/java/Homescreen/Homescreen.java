package Homescreen;

import Level.Level;
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
import helper.Gameinfo;

public class Homescreen implements Screen {

    private Stage stage;
    private Image backgroundImage;
    private Texture backgroundTexture;
    private Texture titleTexture;
    private Texture playButtonTexture;
    private Image titleImage;
    private ImageButton playButton;
    private Texture quitButtonTexture;
    private ImageButton quitButton;
    private Texture menuButtonTexture;
    private ImageButton menuButton;
    private Texture soundButtonTexture;
    private ImageButton soundButton;
    private Main game;

    public Homescreen(Main game) {
        this.game = game;
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        // Load and set background image
        backgroundTexture = new Texture(Gdx.files.internal("Homescreen.jpeg"));
        backgroundImage = new Image(backgroundTexture);
        backgroundImage.setSize(Gameinfo.width, Gameinfo.height);
        stage.addActor(backgroundImage);

        // Load title texture and set up title image
//        titleTexture = new Texture(Gdx.files.internal("text.png"));
//        titleImage = new Image(titleTexture);
//        titleImage.setSize(Gameinfo.width - 300, Gameinfo.height - 300);
//        titleImage.setPosition((Gameinfo.width - titleImage.getWidth()) / 2,
//            Gameinfo.height - titleImage.getHeight() + 80);
//        stage.addActor(titleImage);

        // Load play button texture and set up play button image
        playButtonTexture = new Texture(Gdx.files.internal("play.png"));
        playButton = new ImageButton(new TextureRegionDrawable(playButtonTexture)); // Fix
        playButton.setSize(200, 100);  // Optional: Explicit size for button
        playButton.setPosition((Gameinfo.width - playButton.getWidth()) / 2,
            Gameinfo.height / 2);
        stage.addActor(playButton);

        // Load quit button texture
        quitButtonTexture = new Texture(Gdx.files.internal("quit.png"));
        quitButton = new ImageButton(new TextureRegionDrawable(quitButtonTexture));
        quitButton.setSize(60, 60);
        quitButton.setPosition(10, 10);
        stage.addActor(quitButton);

        // Load menu button texture and add after background
        menuButtonTexture = new Texture(Gdx.files.internal("Homemenu.png"));
        menuButton = new ImageButton(new TextureRegionDrawable(menuButtonTexture));
        menuButton.setPosition(1140,10);
        menuButton.setSize(50, 50);
        stage.addActor(menuButton);

        soundButtonTexture = new Texture(Gdx.files.internal("sound.png"));
        soundButton = new ImageButton(new TextureRegionDrawable(soundButtonTexture));
        soundButton.setPosition(1080,8);
        soundButton.setSize(50, 50);
        stage.addActor(soundButton);

        // Add click listener to the play button
        playButton.addListener(new com.badlogic.gdx.scenes.scene2d.utils.ClickListener() {
            @Override
            public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
                game.setScreen(new Level(game)); // Transition to Level selection screen
            }
        });

        // Add click listener to the quit button
        quitButton.addListener(new com.badlogic.gdx.scenes.scene2d.utils.ClickListener() {
            @Override
            public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
                Gdx.app.exit(); // Quit the game
            }
        });
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(Gameinfo.width, Gameinfo.height, true);
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
        titleTexture.dispose();
        playButtonTexture.dispose();
        quitButtonTexture.dispose();
        menuButtonTexture.dispose();
        soundButtonTexture.dispose();
    }
}
