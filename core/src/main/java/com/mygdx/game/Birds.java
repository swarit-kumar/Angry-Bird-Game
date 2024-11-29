package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;


public abstract class Birds implements Serializable {
    private static final long serialVersionUID = 1L;
    private Sprite sprite;
    private boolean isLaunched;
    private Body body; // Box2D Body
    private World world;
    private boolean isActive;
    protected int impactPower;
    private boolean destroyed = false;
    private boolean isUsed = false;
    private boolean isOutOfScreen = false;
    private float x;
    private float y;

    public Birds(Texture texture, float x, float y, World world, int impactPower) {
        this.sprite = new Sprite(texture);
        this.sprite.setPosition(x, y);
        this.world = world;
        this.isActive = true;
        this.isLaunched = false;
        this.impactPower = impactPower;
        this.x = x;
        this.y = y;

        // Create Box2D body for the bird
        createBody(world, x, y);
    }
    public boolean isLaunched() {
        return isLaunched;
    }
    public void update() {
        if (body != null) {
            Vector2 bodyPosition = body.getPosition();
            setPosition(bodyPosition.x - getWidth() / 2f, bodyPosition.y - getHeight() / 2f);
        }
    }
    public void updatePosition() {
        this.getPosition().x = body.getPosition().x - (this.getTexture().getWidth() / 2);
        this.getPosition().y = body.getPosition().y - (this.getTexture().getHeight() / 2);
    }

    public abstract void update(float delta);
    public float getX() { return x; }
    public float getY() { return y; }
//    public int getHealth() { return health; }

    public abstract void createBody(World world);

    // Abstract method for body creation, which accepts the world and x/y positions
    public abstract void createBody(World world, float x, float y);

//    public void update() {
//        if (body != null) {
//            Vector2 bodyPosition = body.getPosition();
//            sprite.setPosition(bodyPosition.x - sprite.getWidth() / 2, bodyPosition.y - sprite.getHeight() / 2);
//        }
//    }

    // Called when the bird impacts something (like a Pig or Block)
    public void onImpact(Pig pig, Block block) {
        // Add logic to handle impact with pigs and blocks here
    }
    private void writeObject(ObjectOutputStream oos) throws IOException {
        oos.defaultWriteObject(); // Serialize non-transient fields
        oos.writeUTF(sprite.getTexture().toString()); // Serialize texture path
    }

    private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException {
        ois.defaultReadObject(); // Deserialize non-transient fields
        String texturePath = ois.readUTF(); // Read texture path
        this.sprite = new Sprite(new Texture(texturePath)); // Recreate sprite
        if (world != null) {
            createBody(world, x, y); // Recreate body in the physics world
        }
    }

    // Get the position of the bird (Box2D position or Sprite position)
    public Vector2 getPosition() {
        if (body != null) {
            return body.getPosition(); // Retrieve position from Box2D body
        } else {
            return new Vector2(sprite.getX(), sprite.getY());
        }
    }

    // Create the Box2D body for the bird
    private void createBody(float x, float y) {
        if (world == null) {
            System.err.println("World is null. Cannot create body.");
            return; // Early exit if world is not initialized
        }

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(x / GameConstants.PPM, y / GameConstants.PPM);  // Apply PPM (Pixels per Meter)

        CircleShape shape = new CircleShape();
        shape.setRadius(sprite.getWidth() / 2 / GameConstants.PPM); // Set radius based on the sprite width

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1.0f;
        fixtureDef.friction = 0.5f;
        fixtureDef.restitution = 0.3f; // Bounciness

        body = world.createBody(bodyDef);
        body.createFixture(fixtureDef).setUserData(this);  // Assign the bird instance as user data

        shape.dispose(); // Dispose shape after use to avoid memory leak
    }

    // Abstract method to update specific behavior in subclasses
//    public abstract void update(float delta);

    // Draw the bird sprite to the screen
    public void draw(SpriteBatch batch) {
        if (isActive) {
            sprite.draw(batch);
        }
    }

    // Dispose of resources
    public void dispose() {
        sprite.getTexture().dispose();
        if (body != null) {
            world.destroyBody(body);  // Destroy the Box2D body when the bird is disposed
        }
    }

    // Get the Box2D body associated with the bird
    public Body getBody() {
        return this.body;
    }

    // Get the sprite associated with the bird
    public Sprite getSprite() {
        return sprite;
    }

    // Launch the bird with a specific velocity
    public void launch(Vector2 velocity) {
        if (body != null) {
            body.setLinearVelocity(velocity.scl(10)); // Scale the velocity for more realistic speed
            body.setActive(true);
        }
    }

    // Set the position of both the sprite and Box2D body
    public void setPosition(float x, float y) {
        sprite.setPosition(x, y);  // Update the sprite's position

        if (body != null) {
            // Set the Box2D body position (converted to meters)
            body.setTransform(x / GameConstants.PPM, y / GameConstants.PPM, body.getAngle());
        }
    }


    // Get the bounds of the bird for collision detection
    public Rectangle getBounds() {
        // Assuming getPosition() returns the center of the sprite
        float x = getPosition().x - sprite.getWidth() / 2;  // Subtract half the width
        float y = getPosition().y - sprite.getHeight() / 2;  // Subtract half the height
        return new Rectangle(x, y, sprite.getWidth(), sprite.getHeight());
    }


    // Get the texture associated with the bird
    public Texture getTexture() {
        return sprite.getTexture();
    }

    public void setBody(Body body) {
        this.body = body;
    }

    public int getWidth() {
        return (int) sprite.getWidth(); // Assumes `sprite` is the Sprite instance of the bird
    }

    public int getHeight() {
        return (int) sprite.getHeight(); // Assumes `sprite` is the Sprite instance of the bird
    }
    public void setRotation(float degrees) {
        sprite.setRotation(degrees);
    }


    public int getDamage() {
        return 100;
    }

    public void setLaunched(boolean b) {
        this.isLaunched = b;
    }

    public boolean setDestroyed(boolean b) {
        return  this.destroyed = b;
    }
    public boolean isDestroyed() {
        return destroyed;
    }
    public boolean isOutOfScreen() {
        return isOutOfScreen;
    }

    public void setOutOfScreen(boolean outOfScreen) {
        isOutOfScreen = outOfScreen;
    }
    public boolean isUsed() {
        return isUsed;
    }

    public void setUsed(boolean used) {
        isUsed = used;
    }

    public World getWorld() {
        return world;
    }

    public int getImpactPower() {
        return impactPower;
    }
}
