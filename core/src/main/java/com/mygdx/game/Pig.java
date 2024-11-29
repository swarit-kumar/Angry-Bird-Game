package com.mygdx.game;

import Playscreen.PlayScreen;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;


public class Pig implements Serializable {
    private static final long serialVersionUID = 1L;
    private PlayScreen playScreen;
    private Sprite sprite;
    private float health;
    private Body body;  // Box2D Body
    private World world;
    private boolean isActive;

    public Pig(Texture texture, float x, float y, int health, World world) {
        this.sprite = new Sprite(texture);
        this.sprite.setPosition(x, y);
        this.world = world;
        this.isActive = true;
        this.health = 100f;

        // Create Box2D body for the pig
        createBody(x, y);
    }
    private void writeObject(ObjectOutputStream oos) throws IOException {
        oos.defaultWriteObject(); // Serialize non-transient fields
        oos.writeUTF(sprite.getTexture().toString()); // Serialize texture path
    }

    private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException {
        ois.defaultReadObject();  // Deserialize non-transient fields
        String texturePath = ois.readUTF();  // Read texture path
        this.sprite = new Sprite(new Texture(texturePath));  // Recreate sprite

        // Recreate the Box2D body in the world (this requires world reference)
        if (world != null) {
            createBody(sprite.getX(), sprite.getY());  // Create the body again after deserialization
        }
    }


    //    public Pig(Vector2 position, float health) {
//
//    }
    public float getX() {
        if (body != null) {
            return body.getPosition().x * GameConstants.PPM; // Convert Box2D world units to screen pixels
        } else {
            return sprite.getX(); // Fallback to sprite position if body doesn't exist
        }
    }

    public float getY() {
        if (body != null) {
            return body.getPosition().y * GameConstants.PPM; // Convert Box2D world units to screen pixels
        } else {
            return sprite.getY(); // Fallback to sprite position if body doesn't exist
        }
    }


    private void createBody(float x, float y) {
        // Define the Box2D body
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(x / GameConstants.PPM, y / GameConstants.PPM);  // Convert to Box2D world units (meters)

        // Create a circular shape for the pig
        CircleShape shape = new CircleShape();
        shape.setRadius(sprite.getWidth() / 2 / GameConstants.PPM); // Use the radius of the sprite for the Box2D shape

        // Define fixture properties
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1.0f;
        fixtureDef.friction = 0.5f;
        fixtureDef.restitution = 0.3f; // Bounciness when colliding

        // Create the Box2D body and attach the fixture
        body = world.createBody(bodyDef);
        body.createFixture(fixtureDef).setUserData(this); // Store reference to the pig in the fixture for collision detection

        shape.dispose(); // Dispose of shape after it's used
    }
    public void hit() {
        health -= 100f;  // Reduce health when hit (you can adjust the value as needed)

        // If health drops to zero or below, the pig is "destroyed"
        if (health <= 0) {
            onDestroyed();  // Method to handle destruction (e.g., removing from the world)
        }

        // You can add more behavior here (e.g., triggering sound, animations, etc.)
    }

    public void takeDamage(int damage) {
        if (isActive) {
            health -= damage;
            if (health <= 0) {
                onDestroyed();
            }
        }
    }

    private void onDestroyed() {
        // Handle the pig's destruction (remove it from the physics world)
        isActive = false;
//        world.destroyBody(body);Destroy the body from the Box2D world
        playScreen.markForDestruction(body);
        // Dispose of the texture and any other resources
        dispose();
    }

    public void draw(SpriteBatch batch) {
        if (isActive) {
            // Update sprite position to match Box2D body position
            sprite.setPosition(
                (body.getPosition().x * GameConstants.PPM) - sprite.getWidth() / 2,
                (body.getPosition().y * GameConstants.PPM) - sprite.getHeight() / 2
            );
            sprite.draw(batch);
        }
    }

    public void dispose() {
        sprite.getTexture().dispose();
    }

    public Sprite getSprite() {
        return sprite;
    }

    public Body getBody() {
        return body;
    }

    public boolean isActive() {
        return isActive;
    }
    public Vector2 getPosition() {
        if (body != null) {
            return body.getPosition(); // Retrieve position from Box2D body
        } else {
            return new Vector2(sprite.getX(), sprite.getY());
        }
    }

    public void setBody(Body body) {
        this.body = body;
    }

    public Texture getTexture() {
        return sprite.getTexture();
    }

    public float getHealth() {
        return this.health;
    }

    public void destroy() {
        if (!isActive) {
            isActive = false;
            world.destroyBody(body);

        }
    }
//
//    public Object getX() {
//    }
}
