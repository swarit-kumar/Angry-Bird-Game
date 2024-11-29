package com.mygdx.game;

import Playscreen.PlayScreen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;

public class Block implements Serializable {
    private static final long serialVersionUID = 1L;
    private PlayScreen playScreen;
    private Sprite sprite;
    private int durability;
    private Body body;  // Box2D Body
    private World world;
    private boolean isActive;
    private String material;

    public Block(Texture texture, float x, float y, String material, World world) {
        this.sprite = new Sprite(texture);
        this.sprite.setPosition(x, y);
        this.world = world;
        this.material = material;
        this.isActive = true;

        // Create Box2D body for the block
        createBody(x, y);

        // Set durability based on material
        switch (material) {
            case "wood":
                durability = 1;
                break;
            case "glass":
                durability = 2;
                break;
            case "steel":
                durability = 3;
                break;
            default:
                durability = 1; // Default to wood if no material is provided
                break;
        }
    }

    private void createBody(float x, float y) {
        // Define the Box2D body
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(x / GameConstants.PPM, y / GameConstants.PPM);  // Convert to Box2D world units (meters)

        // Create a rectangular shape for the block
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(sprite.getWidth() / 2 / GameConstants.PPM, sprite.getHeight() / 2 / GameConstants.PPM); // Half width/height for Box2D

        // Define fixture properties
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1.0f;
        fixtureDef.friction = 0.5f;
        fixtureDef.restitution = 0.3f; // Bounciness when colliding

        // Create the Box2D body and attach the fixture
        body = world.createBody(bodyDef);
        body.createFixture(fixtureDef).setUserData(this); // Store reference to the block in the fixture for collision detection

        shape.dispose(); // Dispose of shape after it's used
    }

    public void takeDamage(int damage) {
        if (isActive) {
            durability -= damage;
            if (durability <= 0) {
                onCollapse();
            }
        }
    }

    private void onCollapse() {
        // Handle the block's collapse (remove it from the physics world)
        isActive = false;
//        world.destroyBody(body);
// Destroy the body from the Box2D world
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

    public void breakBlock() {

    }
    public Texture getTexture() {
        return sprite.getTexture();
    }

    public int getHealth() {
        return durability;
    }
    public String getMaterial() {
        return material;
    }

    public void destroy() {
        if(!isActive) {
            isActive = false;
            world.destroyBody(body);
        }
    }
    public boolean isDestroyed() {
        return isActive;
    }

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
    private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException {
        ois.defaultReadObject();  // Deserialize non-transient fields
        String texturePath = ois.readUTF();  // Read texture path
        this.sprite = new Sprite(new Texture(texturePath));  // Recreate sprite

        // Set the material and durability
        if (material != null) {
            switch (material) {
                case "wood":
                    durability = 1;
                    break;
                case "glass":
                    durability = 2;
                    break;
                case "steel":
                    durability = 3;
                    break;
                default:
                    durability = 1;
            }
        }

        // Recreate the Box2D body in the world (this requires world reference)
        if (world != null) {
            createBody(sprite.getX(), sprite.getY());  // Create the body again after deserialization
        }
    }

}
