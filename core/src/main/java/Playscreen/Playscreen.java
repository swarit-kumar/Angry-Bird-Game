package Playscreen;

import Menu.Menuscreen;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Queue;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.mygdx.game.*;
import Level.Level;
import helper.Gameinfo;
import com.badlogic.gdx.physics.box2d.*;

import static com.mygdx.game.GameConstants.PPM;

public class PlayScreen implements Screen, InputProcessor {
    private static final float MAX_DRAG_DISTANCE = 100f;
    private Array<Vector2> trajectoryPoints = new Array<>();
    private Main game;
    private int currentLevel;
    private Slingshot slingshot;
    private SpriteBatch batch;
    private ShapeRenderer shapeRenderer;
    private Array<Birds> birds;
    private Array<Pig> pigs;
    private Array<Block> blocks;
    private Texture background;
    private OrthographicCamera camera;
    private FitViewport viewport;;


    private boolean isDragging = false;
    private Birds currentBird;
    private Vector2 launchStart = new Vector2();
    private Vector2 launchEnd = new Vector2();
    private int birdIndex = 0;
    private Texture menuTexture;
    private ImageButton menuButton;
    private Stage stage;
    private final Array<Body> bodiesToDestroy = new Array<>();


    // Box2D related variables
    private World world;
    private Box2DDebugRenderer debugRenderer;

    public PlayScreen(Main game, int level) {
        try {
            this.game = game;
            world = new World(new Vector2(0, -9.8f), true);  // Gravity is typically (0, -9.8f)

            this.camera = new OrthographicCamera();
            this.viewport = new FitViewport(Gameinfo.width, Gameinfo.height, camera);
            this.slingshot = new Slingshot(new Vector2(280, 80), world);

            this.batch = new SpriteBatch();
            this.shapeRenderer = new ShapeRenderer();
            this.birds = new Array<>();
            this.pigs = new Array<>();
            this.blocks = new Array<>();
            this.background = new Texture("background.jpeg");

            this.stage = new Stage(viewport, batch);

            menuTexture = new Texture(Gdx.files.internal("menu.png"));
            menuButton = new ImageButton(new TextureRegionDrawable(menuTexture));
            menuButton.setPosition(10, Gdx.graphics.getHeight() - 60);
            menuButton.setSize(50, 50);
            menuButton.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    game.setScreen(new Menuscreen(game)); // Switch to MenuScreen
                }
            });

            stage.addActor(menuButton);
            Gdx.input.setInputProcessor(stage);
                // Add menuButton to stage

            debugRenderer = new Box2DDebugRenderer();
            world.setContactListener(new GameContactListener(this));

            setupLevel(level);
        }catch (Exception e) {
            Gdx.app.error("PlayScreen", "Error initializing PlayScreen", e);
            // Optionally, return to main menu or show an error screen
            game.setScreen(new Menuscreen(game));
        }

    }
    public void markForDestruction(Body body) {
        synchronized (bodiesToDestroy) {
            if (body != null) {
                Object userData = body.getUserData();
                System.out.println("Marking for destruction: " +
                    (userData != null ? userData.getClass().getSimpleName() : "null body"));
                bodiesToDestroy.add(body);
            } else {
                System.out.println("Attempted to mark null body for destruction");
            }
        }
    }


    private void setupLevel(int level) {
        birds.clear();
        pigs.clear();
        blocks.clear();

        switch (level) {
            case 1:
                loadLevel1();
                break;
            case 2:
                loadLevel2();
                break;
            case 3:
                loadLevel3();
                break;
            default:
                System.out.println("Invalid level! Returning to Level Selection.");
                game.setScreen(new Level(game));
                return; // Exit early if level is invalid
        }

        if (!birds.isEmpty()) {
            currentBird = birds.get(birdIndex);
            Vector2 slingshotPosition = slingshot.getPosition();
            currentBird.setPosition(slingshotPosition.x + 10, slingshotPosition.y + 20);

            if (currentBird.getBody() != null) {
                currentBird.getBody().setTransform(slingshotPosition.x + 10, slingshotPosition.y + 20, 0);  // Set body position

            }else {
                System.out.println("No birds found!");
            }
            currentBird.getBody().setGravityScale(0f);
        }
    }

    private void loadLevel1() {
        RedBird redBird = new RedBird(new Texture(Gdx.files.internal("redbird.png")), 280, 80, 1, world);
        redBird.getSprite().setSize(50, 50);
        birds.add(redBird);
        createBox2DBird(redBird);

        Pig pig = new Pig(new Texture(Gdx.files.internal("pig.png")), 550, 80, 100, world);
        pig.getSprite().setSize(50f, 50f);
        pigs.add(pig);
        createBox2DPig(pig);
        Pig pig1 = new Pig(new Texture(Gdx.files.internal("pig.png")), 500, 100, 100, world);
        pig1.getSprite().setSize(50f, 50f);
        pigs.add(pig1);
        createBox2DPig(pig1);

        Block block = new Block(new Texture(Gdx.files.internal("woodblock.png")), 450, 200, "wood", world);
        block.getSprite().setSize(60, 30);
        blocks.add(block);
        createBox2DBlock(block);
    }

    private void loadLevel2() {
        YellowBird yellowBird = new YellowBird(new Texture(Gdx.files.internal("yellowbird.png")), 280, 80, 2, world);
        yellowBird.getSprite().setSize(50, 50);
        birds.add(yellowBird);
        createBox2DBird(yellowBird);
        RedBird redBird = new RedBird(new Texture(Gdx.files.internal("redbird.png")), 200, 80, 1, world);
        redBird.getSprite().setSize(50, 50);
        redBird.getSprite().setPosition(200, 80);
        birds.add(redBird);
        createBox2DBird(redBird);

        Pig pig = new Pig(new Texture(Gdx.files.internal("pig.png")), 600, 150, 2, world);
        pig.getSprite().setSize(50, 50);
        pigs.add(pig);
        createBox2DPig(pig);

        Block block = new Block(new Texture(Gdx.files.internal("woodblock.png")), 550, 120, "wood", world);
        block.getSprite().setSize(60, 30);
        blocks.add(block);
        createBox2DBlock(block);
    }

    private void loadLevel3() {
        BlackBird blackBird = new BlackBird(new Texture(Gdx.files.internal("blackbird.png")), 280, 80, 2, world);
        blackBird.getSprite().setSize(50, 50);
        birds.add(blackBird);
        createBox2DBird(blackBird);

        Pig pig = new Pig(new Texture(Gdx.files.internal("pig.png")), 700, 200, 3, world);
        pig.getSprite().setSize(50, 50);
        pigs.add(pig);
        createBox2DPig(pig);

        Block block = new Block(new Texture(Gdx.files.internal("steelblock.png")), 650, 180, "steel", world);
        block.getSprite().setSize(60, 30);
        blocks.add(block);
        createBox2DBlock(block);
    }

    private void createBox2DBird(Birds bird) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;  // Bird is DynamicBody for physics interaction
        bodyDef.position.set(bird.getPosition());

        CircleShape shape = new CircleShape();
        shape.setRadius(25 / PPM); // Make sure to account for your pixel-to-meter ratio (PPM)

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1.0f;
        fixtureDef.restitution = 0.5f; // Make sure the restitution is set properly for bounce behavior

        // Create the bird body and fixture
        Body body = world.createBody(bodyDef);
        Fixture fixture = body.createFixture(fixtureDef);
        fixture.setUserData(bird);
        body.setUserData(bird);

        // Set gravity scale for dynamic body (allow it to fall with gravity)
        body.setGravityScale(1f);  // Adjust if you want to change how gravity affects it

        // Make sure the bird can interact with physics world immediately
        body.setActive(true);

        // Set the collision filter for the bird
        Filter birdFilter = new Filter();
        birdFilter.categoryBits = 0x0002;  // Birds category
        birdFilter.maskBits = 0x0004 | 0x0008;  // Birds collide with pigs, blocks
        fixture.setFilterData(birdFilter);

        // Save the reference to the bird's body
        bird.setBody(body);

        // Clean up shape after use
        shape.dispose();
    }
    private void createBox2DPig(Pig pig) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody; // Pigs remain stationary (static)
        bodyDef.position.set(pig.getPosition());

        CircleShape shape = new CircleShape();
        shape.setRadius(25 / PPM);  // Adjusting radius for the physics body

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 0.5f; // Adjust density for the pig's body

        Body body = world.createBody(bodyDef);
        Fixture fixture = body.createFixture(fixtureDef);
        fixture.setUserData(pig);
        body.setUserData(pig);

        // Set the collision filter for the pig
        Filter pigFilter = new Filter();
        pigFilter.categoryBits = 0x0004;  // Pigs category
        pigFilter.maskBits = 0x0002 | 0x0008;  // Pigs collide with birds and blocks
        fixture.setFilterData(pigFilter);

        pig.setBody(body);

        // Clean up shape after use
        shape.dispose();
    }
    private void createBox2DBlock(Block block) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;  // Blocks are static
        bodyDef.position.set(block.getPosition());

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(30 / PPM, 15 / PPM);  // Define block size, accounting for PPM

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1.0f; // Adjust density for the block

        Body body = world.createBody(bodyDef);
        Fixture fixture = body.createFixture(fixtureDef);
        fixture.setUserData(block);
        body.setUserData(block);

        // Set the collision filter for the block
        Filter blockFilter = new Filter();
        blockFilter.categoryBits = 0x0008;  // Block category
        blockFilter.maskBits = 0x0002 | 0x0004;  // Blocks collide with birds and pigs
        fixture.setFilterData(blockFilter);

        block.setBody(body);

        // Clean up shape after use
        shape.dispose();
    }





    private void drawGameElements() {
        for (Pig pig : pigs) pig.getSprite().draw(batch);
        for (Block block : blocks) block.getSprite().draw(batch);
        for (Birds bird : birds) bird.getSprite().draw(batch);
    }

    @Override
    public boolean keyDown(int i) {
        return false;
    }

    @Override
    public boolean keyUp(int i) {
        return false;
    }

    @Override
    public boolean keyTyped(char c) {
        return false;
    }
    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        // Convert screen coordinates to world coordinates
        Vector3 touchPos = camera.unproject(new Vector3(screenX, screenY, 0));

        // Ensure the slingshot contains the touch position
        if (slingshot.contains(touchPos.x, touchPos.y)) {
            isDragging = true;
            launchStart.set(touchPos.x, touchPos.y);
            System.out.println("Dragging started at: " + touchPos);

            trajectoryPoints.clear();
            return true;
        }
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        if (isDragging) {
            // Convert touch position to world coordinates
            Vector3 touchPos = camera.unproject(new Vector3(screenX, screenY, 0));
            launchEnd.set(touchPos.x, touchPos.y);

            // Limit drag distance (you can adjust this based on game balance)
            float dragDistance = launchStart.dst(launchEnd);
            if (dragDistance > MAX_DRAG_DISTANCE) {
                launchEnd.set(launchStart.x + (launchEnd.x - launchStart.x) * (MAX_DRAG_DISTANCE / dragDistance),
                    launchStart.y + (launchEnd.y - launchStart.y) * (MAX_DRAG_DISTANCE / dragDistance));
            }

            // Update bird's position in physics world (Box2D)
            if (currentBird != null && currentBird.getBody() != null) {
                currentBird.getSprite().setPosition(launchEnd.x, launchEnd.y);
                currentBird.getBody().setTransform(launchEnd.x, launchEnd.y, 0);  // Update the physics body's position
            }

            // Calculate and store trajectory points
            calculateTrajectory();
            return true;
        }
        return false;
    }



    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        if (isDragging) {
            isDragging = false;

            if (currentBird != null && currentBird.getBody() != null) {
                // Ensure the bird is not launched yet, it is positioned in the slingshot
                if (!currentBird.isLaunched()) {
                    Vector2 slingshotPosition = slingshot.getPosition();
                    currentBird.getBody().setTransform(slingshotPosition.x + 10, slingshotPosition.y + 20, 0);

                    // Deactivate gravity and physics before launching
                    currentBird.getBody().setGravityScale(0f);  // Gravity is off
                    currentBird.getBody().setLinearVelocity(0, 0);  // Reset velocity

                    currentBird.getBody().setActive(true);  // Make it active for drag
                    System.out.println("Bird set to slingshot position, gravity disabled.");
                }


                // Calculate the launch vector based on the drag distance
                Vector2 launchVector = new Vector2(launchStart.x - launchEnd.x, launchStart.y - launchEnd.y).scl(5f);
                currentBird.getBody().setLinearVelocity(launchVector);

                // Apply an impulse to launch the bird
                Vector2 force = new Vector2(launchVector.x * 50, launchVector.y * 50);
                currentBird.getBody().applyLinearImpulse(force, currentBird.getBody().getWorldCenter(), true);

                // Reactivate gravity after launch
                currentBird.getBody().setGravityScale(1f);
                if (currentBird instanceof YellowBird) {
                    ((YellowBird) currentBird).setLaunched();
                    System.out.println("Bird is launched.");
                }// Gravity back on after launch

                System.out.println("Bird launched with force. Gravity applied.");

                // Transition to the next bird
                birdIndex++;
                if (birdIndex < birds.size) {
                    // Reset the position for the next bird in the slingshot
                    currentBird = birds.get(birdIndex);
                    Vector2 slingshotPositionNew = slingshot.getPosition();
                    currentBird.getSprite().setPosition(slingshotPositionNew.x + 10, slingshotPositionNew.y + 20);
                    currentBird.getBody().setTransform(slingshotPositionNew.x+10, slingshotPositionNew.y+20, 0);

                    // Disable gravity for the next bird until launched
                    currentBird.getBody().setGravityScale(0f); // Gravity is off for now
                    currentBird.getBody().setActive(false); // Make it inactive until it's launched
                    System.out.println("Next bird positioned at slingshot, gravity disabled.");
                } else {
                    currentBird = null; // No more birds left
                }
            }

            // Clear drag points
            launchStart.setZero();
            launchEnd.setZero();
            return true;
        }
        return false;
    }
    private void calculateTrajectory() {
        trajectoryPoints.clear();
        Vector2 launchVector = new Vector2(launchStart.x - launchEnd.x, launchStart.y - launchEnd.y).scl(10f); // Adjust scaling for speed

        // Simulate the bird's path, considering gravity and time
        Vector2 position = new Vector2(launchEnd.x, launchEnd.y);
        Vector2 velocity = launchVector.cpy().scl(1 / 60f); // Scale the velocity for time step

        for (int i = 0; i < 50; i++) { // Generate up to 50 points
            trajectoryPoints.add(new Vector2(position));
            velocity.add(0, -9.8f * 1 / 60f); // Apply gravity
            position.add(velocity);
        }
    }

    private void drawTrajectory() {
        if (isDragging) {
            shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
            shapeRenderer.setColor(1, 1, 0, 1);  // Set trajectory color to red
            for (Vector2 point : trajectoryPoints) {
                shapeRenderer.circle(point.x, point.y, 5);  // Draw circles along the trajectory
            }
            shapeRenderer.end();
        }
    }





    @Override
    public boolean mouseMoved(int i, int i1) {
        return false;
    }

    @Override
    public boolean scrolled(float v, float v1) {
        return false;
    }

    @Override
    public boolean touchCancelled(int i, int i1, int i2, int i3) {
        return false;
    }

    private void launchBird() {
        // Calculate the velocity based on the drag distance and direction
        Vector2 velocity = launchStart.cpy().sub(launchEnd).scl(10f); // Scale the velocity for appropriate launch speed

        // Set the bird's velocity and release it from the slingshot
        currentBird.getBody().setLinearVelocity(velocity);
        currentBird.getBody().setActive(true); // Activate the bird after launch

        birdIndex++;  // Move to the next bird
        if (birdIndex < birds.size) {
            currentBird = birds.get(birdIndex);
            Vector2 slingshotPosition = slingshot.getPosition();
            currentBird.setPosition(slingshotPosition.x + 10, slingshotPosition.y + 20);
        }
    }


    @Override
    public void show() {
        if (world == null) {  // Ensure the world is created only once
            world = new World(new Vector2(0, -9.8f), true); // Gravity
        }

//        // Create the bodies (bird, pig, block) here, before the first step
//        createBox2DBird(bird);
//        createBox2DPig(pig);
//        createBox2DBlock(block);
        Gdx.input.setInputProcessor(this);



    }


    @Override
    public void render(float delta) {
        // Clear the screen and prepare for rendering
        try {

            Gdx.gl.glClearColor(0, 0, 0, 1);
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
            if (world != null) {
                try {
                    world.step(1 / 60f, 6, 2);
                    camera.update();
                    batch.setProjectionMatrix(camera.combined);

                    batch.begin();
                    batch.draw(background, 0, 0, Gameinfo.width, Gameinfo.height);

                    // Draw pigs, blocks, and birds
                    for (Pig pig : pigs) {
                        pig.getSprite().draw(batch);
                    }
                    for (Block block : blocks) {
                        block.getSprite().draw(batch);
                    }
                    for (Birds bird : birds) {
                        if (bird.getBody() != null) {
                            Vector2 bodyPosition = bird.getBody().getPosition();

                            bird.setPosition(bodyPosition.x, bodyPosition.y);
                            bird.draw(batch);
                            bird.setRotation(MathUtils.radiansToDegrees * bird.getBody().getAngle());
                            System.out.println(bird.getBody().getPosition().x+" , "+ bird.getBody().getPosition().y);

                        }
                    }
                    for (Birds bird : birds) {
                        checkBirdOutOfScreen(bird); // Check if the bird is out of screen
                    }
//                    processDestructions();
                    synchronized (bodiesToDestroy) {
                        for (Body body : bodiesToDestroy) {
                            // Remove associated game object
                            Object userData = body.getUserData();
                            if (userData instanceof Pig) {
                                pigs.removeValue((Pig) userData, true);
                            } else if (userData instanceof Block) {
                                blocks.removeValue((Block) userData, true);
                            }

                            // Destroy the physics body
                            world.destroyBody(body);
                        }
                        bodiesToDestroy.clear();
                    }

                    slingshot.draw(batch);
                    batch.end();

                    drawTrajectory();

                    // Render stage for UI
                    stage.act(delta);
                    stage.draw();

                    // Check win condition
                    checkForWinCondition();
                    checkForLossCondition();
//                    System.out.println("Stepping the world...");// Time step: 1/60f (60 FPS), velocity and position iterations
                } catch (AssertionError e) {
                    Gdx.app.error("World Step Error", "Error stepping world: ", e);
                }
            } else {
                Gdx.app.error("World Error", "World is null, can't step the world.");
            }
//            // After the physics step, process removals
//            consolidatePendingRemovals();
//            processPendingRemovals();

            // Update camera and batch for drawing

        } catch (Exception e) {
            Gdx.app.error("Render", "Error in render method", e);
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
        batch.dispose();
        shapeRenderer.dispose();
        background.dispose();
        menuTexture.dispose();
        stage.dispose();
        world.dispose();
        debugRenderer.dispose();

        for (Birds bird : birds) {
            bird.dispose(); // Dispose bird-specific resources
        }
        for (Pig pig : pigs) {
            pig.dispose(); // Dispose pig-specific resources
        }
        for (Block block : blocks) {
            block.dispose(); // Dispose block-specific resources
        }
    }

    public static class GameContactListener implements ContactListener {
        private PlayScreen playScreen;

        public GameContactListener(PlayScreen playScreen) {
            this.playScreen = playScreen;
        }

        @Override
        public void beginContact(Contact contact) {
            Fixture fixtureA = contact.getFixtureA();
            Fixture fixtureB = contact.getFixtureB();

            Object userDataA = fixtureA.getBody().getUserData();
            Object userDataB = fixtureB.getBody().getUserData();

            // Collision between Bird and Pig
            if ((userDataA instanceof Birds && userDataB instanceof Pig) ||
                (userDataA instanceof Pig && userDataB instanceof Birds)) {

                Birds bird = (userDataA instanceof Birds) ? (Birds) userDataA : (Birds) userDataB;
                Pig pig = (userDataA instanceof Pig) ? (Pig) userDataA : (Pig) userDataB;

                if (bird != null && pig != null) {
                    System.out.println("Bird collided with Pig!");

                    // Mark the pig for destruction
                    playScreen.markForDestruction(pig.getBody());

                    // Reduce the bird's speed
                    reduceBirdSpeed(bird);

                    // Optional: Handle additional effects like damage or play sound
                    handleBirdPigCollision(bird, pig);
                }
            }

            // Additional collision handling...
        }

        // This function reduces the bird's speed after the collision with a pig
        private void reduceBirdSpeed(Birds bird) {
            Body birdBody = bird.getBody();

            // Get the current velocity of the bird
            Vector2 velocity = birdBody.getLinearVelocity();

            // Reduce the bird's velocity (you can adjust the multiplier as needed)
            float reducedSpeedX = velocity.x * 0.5f;  // Slow down the X velocity by 50%
            float reducedSpeedY = velocity.y * 0.5f;  // Slow down the Y velocity by 50%

            // Apply the reduced velocity back to the bird's body
            birdBody.setLinearVelocity(reducedSpeedX, reducedSpeedY);
        }


        @Override
        public void endContact(Contact contact) {

        }

        @Override
        public void preSolve(Contact contact, Manifold oldManifold) {

        }

        @Override
        public void postSolve(Contact contact, ContactImpulse impulse) {

        }

        private void handleBirdPigCollision(Birds bird, Pig pig) {
            System.out.println("Bird collided with Pig!");

            // Mark pig for destruction
            playScreen.markForDestruction(pig.getBody());

            // Add more detailed effects if needed
            System.out.println("Handling additional effects for Bird-Pig collision.");
        }

        private void handleBirdBlockCollision(Birds bird, Block block) {
            System.out.println("Bird collided with Block!");

            // Mark block for destruction
            playScreen.markForDestruction(block.getBody());

            // Add more detailed effects if needed
            System.out.println("Handling additional effects for Bird-Block collision.");
        }

        // Utility methods to check types
        private boolean isBird(Object obj) {
            return obj instanceof Birds;
        }

        private boolean isPig(Object obj) {
            return obj instanceof Pig;
        }

        private boolean isBlock(Object obj) {
            return obj instanceof Block;
        }


        private Birds getBird(Fixture fixtureA, Fixture fixtureB) {
            if (isBird(fixtureA)) return (Birds) fixtureA.getUserData();
            if (isBird(fixtureB)) return (Birds) fixtureB.getUserData();
            return null;
        }

        private Pig getPig(Fixture fixtureA, Fixture fixtureB) {
            if (isPig(fixtureA)) return (Pig) fixtureA.getUserData();
            if (isPig(fixtureB)) return (Pig) fixtureB.getUserData();
            return null;
        }

        private Block getBlock(Fixture fixtureA, Fixture fixtureB) {
            if (isBlock(fixtureA)) return (Block) fixtureA.getUserData();
            if (isBlock(fixtureB)) return (Block) fixtureB.getUserData();
            return null;
        }
    }
    public void checkBirdOutOfScreen(Birds bird) {
        if (bird.getBody() != null && isBirdOutOfScreen(bird)) {
            System.out.println("Bird is out of the screen and will be destroyed!");

            // Destroy the body if the bird is out of the screen
            world.destroyBody(bird.getBody());  // Properly destroy the bird's body from Box2D
            bird.setBody(null);  // Remove the reference to the body

            // Mark the bird as destroyed
            bird.setDestroyed(true);

            System.out.println("Bird destroyed.");
        }
    }

    private boolean isBirdOutOfScreen(Birds bird) {
        // Define the screen boundaries, assuming screen width and height
        float screenWidth = 800;  // Example screen width
        float screenHeight = 600; // Example screen height

        float birdX = bird.getPosition().x;
        float birdY = bird.getPosition().y;

        // Check if the bird is outside the screen boundaries
        return birdX < 0 || birdX > screenWidth || birdY < 0 || birdY > screenHeight;
    }




    // Check if all pigs are destroyed
    public void checkForWinCondition() {
        boolean allPigsDestroyed = true;
        boolean allBlocksDestroyed = true;

        // Check if there are any pigs left in the world
        for (Pig pig : pigs) {  // Assume `pigs` is a list of all pigs in the game
            if (pig.isActive()) {  // If any pig is still active, set allPigsDestroyed to false
                allPigsDestroyed = false;
                break;
            }
        }

        // Check if there are any blocks left in the world
        for (Block block : blocks) {  // Assume `blocks` is a list of all blocks in the game
            if (block.isActive()) {  // If any block is still active, set allBlocksDestroyed to false
                allBlocksDestroyed = false;
                break;
            }
        }

        // If all pigs and blocks are destroyed, trigger the win condition
        if (allPigsDestroyed && allBlocksDestroyed) {
            System.out.println("You win!");
            // Optional: Trigger win screen or victory animation
            displayWinScreen();
        }
    }

    public void displayWinScreen() {
        // Show a win message or transition to a victory screen
        System.out.println("Victory! You destroyed all the pigs!");
        // Transition to a win screen, pause the game, etc.
    }


    public void checkForLossCondition() {
        boolean allBirdsDestroyed = true;

        // Iterate over all birds in the game
        for (Birds bird : birds) {  // Assume `birds` is a list of all bird objects in the game
            if (bird.getBody()!=null) {  // If any bird is still active, we haven't lost
                allBirdsDestroyed = false;
                break;  // Exit the loop early since we found an active bird
            }
        }

        // If all birds are destroyed (inactive), trigger the loss condition
        if (allBirdsDestroyed) {
            System.out.println("All birds are destroyed. You lose!");
            // Optional: Trigger loss screen or game over animation
            displayLossScreen();
        }
    }

    private void displayLossScreen() {
        System.out.println("You loose the game");
    }

    // More methods for input processing, bird launching, and collision handling
}
