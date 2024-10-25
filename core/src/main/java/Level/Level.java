package Level;

import Homescreen.Homescreen;
import Playscreen.Playscreen;
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

public class Level implements Screen {

    private Stage stage;
    private Texture backgroundTexture;
    private Image backgroundImage;

    private Texture buttonTexture1;
    private Texture buttonTexture2;
    private Texture buttonTexture3;

    private ImageButton levelButton1;
    private ImageButton levelButton2;
    private ImageButton levelButton3;
    private Texture backButtonTexture;
    private ImageButton backButton;
    private Main game;

    public Level(Main game) {
        this.game = game;
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        backgroundTexture = new Texture(Gdx.files.internal("background.jpeg"));
        backgroundImage = new Image(backgroundTexture);
        backgroundImage.setSize(Gameinfo.width, Gameinfo.height);
        stage.addActor(backgroundImage);

        buttonTexture1 = new Texture(Gdx.files.internal("level1.png"));
        levelButton1 = new ImageButton(new TextureRegionDrawable(buttonTexture1));
        levelButton1.setSize(200, 100);
        levelButton1.setPosition(300, 250);
        stage.addActor(levelButton1);

        buttonTexture2 = new Texture(Gdx.files.internal("level2.png"));
        levelButton2 = new ImageButton(new TextureRegionDrawable(buttonTexture2));
        levelButton2.setSize(200, 100);
        levelButton2.setPosition(500, 250);
        stage.addActor(levelButton2);

        buttonTexture3 = new Texture(Gdx.files.internal("level3.png"));
        levelButton3 = new ImageButton(new TextureRegionDrawable(buttonTexture3));
        levelButton3.setSize(200, 100);
        levelButton3.setPosition(700, 250);
        stage.addActor(levelButton3);

        levelButton1.addListener(new com.badlogic.gdx.scenes.scene2d.utils.ClickListener() {
            @Override
            public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
                game.setScreen(new Playscreen(game, 1)); // Go to PlayScreen level 1
            }
        });

        levelButton2.addListener(new com.badlogic.gdx.scenes.scene2d.utils.ClickListener() {
            @Override
            public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
                game.setScreen(new Playscreen(game, 2)); // Go to PlayScreen level 2
            }
        });

        levelButton3.addListener(new com.badlogic.gdx.scenes.scene2d.utils.ClickListener() {
            @Override
            public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
                game.setScreen(new Playscreen(game, 3)); // Go to PlayScreen level 3
            }
        });

        // Load back button texture and set up back button
        backButtonTexture = new Texture(Gdx.files.internal("back.png"));
        backButton = new ImageButton(new TextureRegionDrawable(backButtonTexture));
        backButton.setSize(60, 60);
        backButton.setPosition(10, 10);
        stage.addActor(backButton);

        backButton.addListener(new com.badlogic.gdx.scenes.scene2d.utils.ClickListener() {
            @Override
            public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
                game.setScreen(new Homescreen(game)); // Go back to Homescreen
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
    public void hide() { }

    @Override
    public void pause() { }

    @Override
    public void resume() { }

    @Override
    public void dispose() {
        stage.dispose();
        backgroundTexture.dispose();
        buttonTexture1.dispose();
        buttonTexture2.dispose();
        buttonTexture3.dispose();
        backButtonTexture.dispose();
    }
}
